package com.codingyard;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.contest.uva.UVaSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.config.UserConfiguration;
import com.codingyard.dao.TokenDAO;
import com.codingyard.dao.TopCoderSolutionDAO;
import com.codingyard.dao.UVaSolutionDAO;
import com.codingyard.dao.UserDAO;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.manager.UVaSolutionManager;
import com.codingyard.manager.UserManager;
import com.codingyard.resources.UserResource;
import com.codingyard.resources.permission.SolutionPermissionResource;
import com.codingyard.resources.permission.UserPermissionResource;
import com.codingyard.resources.solution.TopCoderSolutionResource;
import com.codingyard.resources.solution.UVaSolutionResource;
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

        addResources(configuration, environment, new UserManager(userDAO));
        addAuthentication(environment, userDAO, tokenDAO);
        prePopulateUsers(configuration.getUsers(), userDAO);
    }

    private void addResources(final CodingyardConfiguration configuration, final Environment environment,
                              final UserManager userManager) {
        final TopCoderSolutionDAO tcDAO = new TopCoderSolutionDAO(hibernate.getSessionFactory());
        final TopCoderSolutionManager tcManager = new TopCoderSolutionManager(tcDAO, configuration.getSolutionDir());

        final UVaSolutionDAO uvaDAO = new UVaSolutionDAO(hibernate.getSessionFactory());
        final UVaSolutionManager uvaManager = new UVaSolutionManager(uvaDAO, configuration.getSolutionDir());
        environment.jersey().register(new TopCoderSolutionResource(userManager, tcManager));
        environment.jersey().register(new UVaSolutionResource(userManager, uvaManager));
        environment.jersey().register(new UserResource(userManager));
        environment.jersey().register(new SolutionPermissionResource(tcManager, uvaManager));
        environment.jersey().register(new UserPermissionResource(userManager));
    }

    private HibernateBundle<CodingyardConfiguration> buildHibernateBundle() {
        return new HibernateBundle<CodingyardConfiguration>(
            CodingyardUser.class,
            CodingyardToken.class,
            Solution.class,
            TopCoderSolution.class,
            UVaSolution.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(CodingyardConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

    private void addAuthentication(final Environment environment, final UserDAO userDAO, final TokenDAO tokenDAO) {
        /**
         * For Basic authentication, we use a custom header "BasicNoPopup" instead of standard "Basic."
         * If we use basic, a browser displays a popup window to enter credentials when 401 is returned by server,
         * and this is not the behaviour users want when they hit an api and get a failure response.
         *
         * One possible solution is to not use Basic as 'WWW-Authenticate'
         *
         * references
         * Dropwizard issue report : https://github.com/dropwizard/dropwizard/issues/798
         * Stack overflow : http://stackoverflow.com/questions/24130308/preventing-http-basic-auth-dialog-using-angularjs-interceptors
         */
        final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
            new BasicAuthFactory<>(new UserCredentialAuthenticator(userDAO), "Basic No Popup User Auth", CodingyardUser.class)
                .prefix("BasicNoPopup"),
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
