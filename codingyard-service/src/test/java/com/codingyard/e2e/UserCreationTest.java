package com.codingyard.e2e;

import com.codingyard.CodingyardService;
import com.codingyard.config.CodingyardConfiguration;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.entity.user.Role;
import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCreationTest {

    private static final String PATH_TO_YML = "e2e-codingyard.yml";
    private static final String USER_API = "user";


    @ClassRule
    public static final DropwizardAppRule<CodingyardConfiguration> RULE =
        new DropwizardAppRule<>(CodingyardService.class, Resources.getResource(PATH_TO_YML).getPath());


    @Test
    public void createUserAndRetrieveInfo() {
        final String username = "TurtleShip";
        final String password = "safePassword123";
        final String firstName = "Seulgi";
        final String lastName = "Kim";
        final Role role = Role.GUEST;

        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        formData.add("username", username);
        formData.add("password", password);
        formData.add("firstName", firstName);
        formData.add("lastName", lastName);
        formData.add("role", role.name());

        ClientResponse response =
            client.resource(String.format("http://localhost:%d/%s", RULE.getLocalPort(), USER_API))
                .post(ClientResponse.class, formData);

        Long userId = response.getEntity(Long.class);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());


        ClientResponse userRetrievalResponse = new JerseyClientBuilder(RULE.getEnvironment()).build("user info client")
            .resource(String.format("http://localhost:%d/%s/%d", RULE.getLocalPort(), USER_API, userId))
            .get(ClientResponse.class);

        assertThat(userRetrievalResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        CodingyardUser user = userRetrievalResponse.getEntity(CodingyardUser.class);

        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isNull(); // shouldn't expose password
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getRole()).isEqualTo(role);
    }

}
