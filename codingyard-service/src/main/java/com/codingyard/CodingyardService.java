package com.codingyard;

import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.dao.UserDAO;
import com.codingyard.entity.auth.CodingyardToken;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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

        environment.jersey().register(new UserResource(userDAO));
        addAuthentication(environment, userDAO);

        /*
         TODO: Add an authenticator. Authenticator should do below
          1) Check user matches who he claims to be.
          2) Return user's permission.
          */
    }

    private HibernateBundle<CodingyardConfiguration> buildHibernateBundle() {
        return new HibernateBundle<CodingyardConfiguration>(
            CodingyardUser.class,
            CodingyardToken.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(CodingyardConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

    private void addAuthentication(final Environment environment, final UserDAO userDAO) {
        environment.jersey().register(
            new BasicAuthProvider<>(new UserCredentialAuthenticator(userDAO), "Basic User Auth")
        );
    }
}
