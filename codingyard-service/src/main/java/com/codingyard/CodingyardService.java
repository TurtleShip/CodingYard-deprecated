package com.codingyard;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.config.UserConfiguration;
import com.codingyard.dao.TokenDAO;
import com.codingyard.dao.TopCoderSolutionDAO;
import com.codingyard.dao.UserDAO;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.manager.UserManager;
import com.codingyard.resources.UserResource;
import com.codingyard.resources.solution.TopCoderSolutionResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CodingyardService extends Application<CodingyardConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(CodingyardService.class);
    private final HibernateBundle<CodingyardConfiguration> hibernate = buildHibernateBundle();

    public static void main(String[] args) throws Exception {
        new CodingyardService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<CodingyardConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final CodingyardConfiguration configuration,
                    final Environment environment) throws Exception {

        final UserDAO userDAO = new UserDAO(hibernate.getSessionFactory());
        final TokenDAO tokenDAO = new TokenDAO(hibernate.getSessionFactory());
        final TopCoderSolutionDAO tcDAO = new TopCoderSolutionDAO(hibernate.getSessionFactory());

        addResources(configuration, environment, new UserManager(userDAO), tcDAO);
        addAuthentication(environment, userDAO, tokenDAO);
        prePopulateUsers(configuration.getUsers(), userDAO);
//        addGlobalAdmin(configuration.getGlobalAdminConfiguration(), userDAO);
    }

    private void addResources(final CodingyardConfiguration configuration, final Environment environment,
                              final UserManager userManager, final TopCoderSolutionDAO tcDAO) {
        environment.jersey().register(new TopCoderSolutionResource(userManager, new TopCoderSolutionManager(tcDAO, configuration.getSolutionDir())));
        environment.jersey().register(new UserResource(userManager));
    }

    private HibernateBundle<CodingyardConfiguration> buildHibernateBundle() {
        return new HibernateBundle<CodingyardConfiguration>(
            CodingyardUser.class,
            CodingyardToken.class,
            Solution.class,
            TopCoderSolution.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(CodingyardConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

    private void addAuthentication(final Environment environment, final UserDAO userDAO, final TokenDAO tokenDAO) {

        final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
            new BasicAuthFactory<>(new UserCredentialAuthenticator(userDAO), "Basic User Auth", CodingyardUser.class),
            new OAuthFactory<>(new TokenAuthenticator(tokenDAO), "Bearer User OAuth", CodingyardUser.class)
        );

        environment.jersey().register(
            AuthFactory.binder(chainedAuthFactory)
        );
    }

    private void prePopulateUsers(final List<UserConfiguration> usersToPopulate, final UserDAO userDAO) {

        Session session = hibernate.getSessionFactory().openSession();
        session.setDefaultReadOnly(false);
        session.setCacheMode(CacheMode.IGNORE);
        session.setFlushMode(FlushMode.ALWAYS);
        ManagedSessionContext.bind(session);

        for (final UserConfiguration config : usersToPopulate) {
            addUser(config, userDAO);
        }

        session.flush();
        session.close();
    }

    private void addUser(final UserConfiguration config, final UserDAO userDAO) {
        final String username = config.getUsername();
        if (userDAO.findByUsername(username).isPresent()) {
            LOG.info("User {} already exists. Not adding the user.", username);
        } else {
            final CodingyardUser user = new CodingyardUser.Builder(username, config.getPassword())
                .firstName(config.getFirstName())
                .lastName(config.getLastName())
                .role(config.getRole())
                .build();
            userDAO.save(user);
            LOG.info("Added user {} to database.", username);
        }
    }
}
