package com.codingyard.dao;

import com.codahale.metrics.MetricRegistry;
import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.Before;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class H2Test {

    protected SessionFactory sessionFactory = buildSessionFactory(
        CodingyardUser.class,
        CodingyardToken.class,
        Solution.class,
        TopCoderSolution.class);

    private Session session;

    @Before
    public void setup() {
        session = sessionFactory.openSession();
        session.setDefaultReadOnly(false);
        session.setCacheMode(CacheMode.IGNORE);
        session.setFlushMode(FlushMode.ALWAYS);
        ManagedSessionContext.bind(session);
    }

    @After
    public void teardown() {
        session.close();
    }


    private SessionFactory buildSessionFactory(Class<?> entity, Class<?>... entities) {
        final SessionFactoryFactory sff = new SessionFactoryFactory();
        final HibernateBundle<TestConfiguration> hibernate = buildHibernateBundle(entity, entities);
        final TestConfiguration dbConfig = buildTestConfiguration();
        final Environment environment = buildTestEnvironment();
        final List<Class<?>> allEntities = ImmutableList.<Class<?>>builder().add(entity).add(entities).build();
        return sff.build(hibernate, environment, dbConfig.getDataSourceFactory(), allEntities);
    }

    private Environment buildTestEnvironment() {
        return new Environment(
            "test",
            Jackson.newObjectMapper(),
            Validation.buildDefaultValidatorFactory().getValidator(),
            new MetricRegistry(),
            Thread.currentThread().getContextClassLoader());
    }

    private TestConfiguration buildTestConfiguration() {
        TestConfiguration config = new TestConfiguration();
        config.getDataSourceFactory().setUrl("jdbc:h2:mem:DbTest-" + System.currentTimeMillis());
        config.getDataSourceFactory().setUser("sa");
        config.getDataSourceFactory().setDriverClass("org.h2.Driver");
        config.getDataSourceFactory().setValidationQuery("SELECT 1");

        Map<String, String> properties = Maps.newHashMap();
        properties.put("dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create");
        config.getDataSourceFactory().setProperties(properties);
        return config;
    }

    private HibernateBundle<TestConfiguration> buildHibernateBundle(Class<?> entity, Class<?>... entities) {
        return new HibernateBundle<TestConfiguration>(entity, entities) {
            @Override
            public DataSourceFactory getDataSourceFactory(TestConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }
}


class TestConfiguration extends io.dropwizard.Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public TestConfiguration() {
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}