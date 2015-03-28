package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.entity.user.Role;
import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
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
        client.destroy();
    }

    @Test
    public void createUserAndRetrieveUserInfo() {
        final CodingyardUser expected = generateRandomUser(Role.GUEST);
        final Long seulgiId = createNewUser(expected);

        final ClientResponse response =
            client.resource(String.format("http://localhost:%d/%s/%d", RULE.getLocalPort(), USER_API, seulgiId))
                .get(ClientResponse.class);

        final CodingyardUser actual = response.getEntity(CodingyardUser.class);

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

        final ClientResponse firstResponse = login(user);
        final ClientResponse secondResponse = login(user);
        final String firstToken = firstResponse.getEntity(String.class);
        final String secondToken = secondResponse.getEntity(String.class);

        assertThat(firstResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(secondResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(firstToken).isNotNull();
        assertThat(firstToken).isNotEmpty();
        assertThat(secondToken).isNotNull();
        assertThat(secondToken).isNotEmpty();
        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    private ClientResponse login(final CodingyardUser user) {
        client.addFilter(new HTTPBasicAuthFilter(user.getUsername(), user.getPassword()));
        return client.resource(String.format("http://localhost:%d/%s/login", RULE.getLocalPort(), USER_API))
            .post(ClientResponse.class);
    }

    private CodingyardUser generateRandomUser(final Role role) {
        return new CodingyardUser(randomString(), randomString(), randomString(), randomString(), role);
    }

    private Long createNewUser(final CodingyardUser user) {

        final ClientResponse response =
            client.resource(String.format("http://localhost:%d/%s", RULE.getLocalPort(), USER_API))
                .post(ClientResponse.class, createFormDataForUser(user));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        return response.getEntity(Long.class);
    }


    private MultivaluedMap<String, String> createFormDataForUser(final CodingyardUser user) {
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        formData.add("username", user.getUsername());
        formData.add("password", user.getPassword());
        formData.add("firstName", user.getFirstName());
        formData.add("lastName", user.getLastName());
        formData.add("role", user.getRole().name());
        return formData;
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }
}
