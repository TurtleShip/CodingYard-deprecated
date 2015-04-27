package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.api.payload.TokenAndUser;
import com.codingyard.client.CodingyardClient;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.config.GlobalAdminConfiguration;
import com.google.common.io.Resources;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreationTest {

    private static final String PATH_TO_YML = "e2e-codingyard.yml";
    private static CodingyardClient CLIENT;

    @ClassRule
    public static final DropwizardAppRule<CodingyardConfiguration> RULE =
        new DropwizardAppRule<>(CodingyardService.class, Resources.getResource(PATH_TO_YML).getPath());

    @BeforeClass
    public static void setupOnce() throws Exception {
        final Client dwClient = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        final URL localServer = new URL(String.format("http://localhost:%d/", RULE.getLocalPort()));
        CLIENT = new CodingyardClient(dwClient, localServer);
    }

    @Test
    public void createUserAndRetrieveUserInfo() {
        final CodingyardUser expected = generateRandomUser(Role.GUEST);
        final Response responseForUserCreation = CLIENT.createUser(expected);
        final Long userId = responseForUserCreation.readEntity(Long.class);
        final Response response = CLIENT.getUser(userId);
        final CodingyardUser actual = response.readEntity(CodingyardUser.class);

        assertThat(responseForUserCreation.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
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
        CLIENT.createUser(user);

        final Response firstResponse = CLIENT.login(user);
        final Response secondResponse = CLIENT.login(user);

        final String firstToken = firstResponse.readEntity(TokenAndUser.class).getToken();
        final String secondToken = secondResponse.readEntity(TokenAndUser.class).getToken();

        assertThat(firstResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(secondResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(firstToken).isNotNull();
        assertThat(firstToken).isNotEmpty();
        assertThat(secondToken).isNotNull();
        assertThat(secondToken).isNotEmpty();
        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    @Test
    public void globalAdminShouldBeCreatedAtStartUp() {
        assertThat(loginAsAdmin().getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void globalAdminCanPromoteGuest() {
        final String adminToken = loginAsAdmin().readEntity(TokenAndUser.class).getToken();
        final Response guestResponse = CLIENT.createUser(generateRandomUser(Role.GUEST));
        final Long guestId = guestResponse.readEntity(Long.class);
        final CodingyardUser guestBeforePromotion = CLIENT.getUser(guestId).readEntity(CodingyardUser.class);
        final Response response = CLIENT.changeRole(adminToken, guestId, Role.MEMBER);
        final CodingyardUser guestAfterPromotion = CLIENT.getUser(guestId).readEntity(CodingyardUser.class);

        assertThat(guestResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(guestBeforePromotion.getRole()).isEqualTo(Role.GUEST);
        assertThat(guestAfterPromotion.getRole()).isEqualTo(Role.MEMBER);
    }

    private CodingyardUser generateRandomUser(final Role role) {
        return new CodingyardUser.Builder(randomString(), randomString())
            .role(role)
            .build();
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    private Response loginAsAdmin() {
        final GlobalAdminConfiguration globalAdmin = RULE.getConfiguration().getGlobalAdminConfiguration();
        final String username = globalAdmin.getUsername();
        final String password = globalAdmin.getPassword();
        return CLIENT.login(username, password);
    }
}
