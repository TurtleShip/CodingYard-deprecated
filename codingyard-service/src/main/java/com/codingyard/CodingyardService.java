package com.codingyard;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.config.GlobalAdminConfiguration;
import com.codingyard.dao.TokenDAO;
import com.codingyard.dao.TopCoderSolutionDAO;
import com.codingyard.dao.UserDAO;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.resources.SolutionResource;
import com.codingyard.resources.UserResource;
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

public class CodingyardService extends Application<CodingyardConfiguration> {

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

        addResources(configuration, environment, userDAO, tcDAO);
        addAuthentication(environment, userDAO, tokenDAO);
        addGlobalAdmin(configuration.getGlobalAdminConfiguration(), userDAO);
    }

    private void addResources(final CodingyardConfiguration configuration, final Environment environment,
                              final UserDAO userDAO, final TopCoderSolutionDAO tcDAO) {
        environment.jersey().register(new SolutionResource(userDAO, new TopCoderSolutionManager(tcDAO, configuration.getSolutionDir())));
        environment.jersey().register(new UserResource(userDAO));
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

    // Can't annotate this with @UnitOfWork since the annotation is meant for only resource endpoints :(
    private void addGlobalAdmin(final GlobalAdminConfiguration config, final UserDAO userDAO) {

        Session session = hibernate.getSessionFactory().openSession();
        session.setDefaultReadOnly(false);
        session.setCacheMode(CacheMode.IGNORE);
        session.setFlushMode(FlushMode.ALWAYS);
        ManagedSessionContext.bind(session);

        final CodingyardUser globalAdmin = new CodingyardUser.Builder(config.getUsername(), config.getPassword())
            .firstName(config.getFirstName())
            .lastName(config.getLastName())
            .role(Role.ADMIN)
            .build();
        userDAO.save(globalAdmin);
        session.flush();
        session.close();
    }
}
