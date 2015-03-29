package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.entity.user.Role;
import com.google.common.io.Resources;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreationTest {

    private static final String PATH_TO_YML = "e2e-codingyard.yml";
    private static final String USER_API = "user";
    private static final Logger LOG = LoggerFactory.getLogger(UserCreationTest.class);
    private static int CLIENT_ID = 0;
    private Client client;


    @ClassRule
    public static final DropwizardAppRule<CodingyardConfiguration> RULE =
        new DropwizardAppRule<>(CodingyardService.class, Resources.getResource(PATH_TO_YML).getPath());

    @Before
    public void setup() {
        LOG.info("Initializing client " + CLIENT_ID);
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client " + CLIENT_ID);
        CLIENT_ID++;
    }

    @After
    public void teardown() {
        client.close();
    }

    @Test
    public void createUserAndRetrieveUserInfo() {
        final CodingyardUser expected = generateRandomUser(Role.GUEST);
        final Response responseForUserCreation = createNewUser(expected);
        final Long userId = responseForUserCreation.readEntity(Long.class);

        assertThat(responseForUserCreation.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        final Response response = client.target(String.format("http://localhost:%d/%s/%d", RULE.getLocalPort(), USER_API, userId))
            .request()
            .get();

        final CodingyardUser actual = response.readEntity(CodingyardUser.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isNull(); // shouldn't expose password
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
    }

    @Test
    public void loginShouldCreateNewToken() {
        final CodingyardUser user = generateRandomUser(Role.GUEST);
        createNewUser(user);

        final Response firstResponse = login(user);
        final Response secondResponse = login(user);
        final String firstToken = firstResponse.readEntity(String.class);
        final String secondToken = secondResponse.readEntity(String.class);

        assertThat(firstResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(secondResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(firstToken).isNotNull();
        assertThat(firstToken).isNotEmpty();
        assertThat(secondToken).isNotNull();
        assertThat(secondToken).isNotEmpty();
        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    private Response login(final CodingyardUser user) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(user.getUsername(), user.getPassword());
        client.register(feature);
        return client.target(String.format("http://localhost:%d/%s/login", RULE.getLocalPort(), USER_API))
            .request()
            .post(Entity.json(null));
    }

    private CodingyardUser generateRandomUser(final Role role) {
        return new CodingyardUser(randomString(), randomString(), randomString(), randomString(), role);
    }

    private Response createNewUser(final CodingyardUser user) {
        return client.target(String.format("http://localhost:%d/%s", RULE.getLocalPort(), USER_API))
            .request()
            .post(Entity.form(createFormDataForUser(user)));
    }


    private Form createFormDataForUser(final CodingyardUser user) {
        return new Form()
            .param("username", user.getUsername())
            .param("password", user.getPassword())
            .param("firstName", user.getFirstName())
            .param("lastName", user.getLastName())
            .param("role", user.getRole().name());
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}
