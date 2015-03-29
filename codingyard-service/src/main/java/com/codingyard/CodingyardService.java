package com.codingyard;

import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.dao.TokenDAO;
import com.codingyard.dao.UserDAO;
import com.codingyard.entity.auth.CodingyardToken;
import com.codingyard.entity.user.CodingyardUser;
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

        environment.jersey().register(new UserResource(userDAO));
        addAuthentication(environment, userDAO, tokenDAO);

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

    private void addAuthentication(final Environment environment, final UserDAO userDAO, final TokenDAO tokenDAO) {

        final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
            new BasicAuthFactory<>(new UserCredentialAuthenticator(userDAO), "Basic User Auth", CodingyardUser.class),
            new OAuthFactory<>(new TokenAuthenticator(tokenDAO), "Bearer User OAuth", CodingyardUser.class)
        );

        environment.jersey().register(
            AuthFactory.binder(chainedAuthFactory)
        );
    }
}
