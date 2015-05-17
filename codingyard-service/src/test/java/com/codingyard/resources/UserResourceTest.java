package com.codingyard.resources;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.manager.UserManager;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserResourceTest {

    private static final UserManager userManager = mock(UserManager.class);
    private static final UserCredentialAuthenticator userCredentialAuthenticator = mock(UserCredentialAuthenticator.class);
    private static final TokenAuthenticator tokenAuthenticator = mock(TokenAuthenticator.class);
    private static final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
        new BasicAuthFactory<>(userCredentialAuthenticator, "Basic User Auth", CodingyardUser.class),
        new OAuthFactory<>(tokenAuthenticator, "Bearer User OAuth", CodingyardUser.class)
    );

    private final CodingyardUser globalAdmin =
        new CodingyardUser.Builder("turtleship", "safe_password")
            .role(Role.GLOBAL_ADMIN)
            .build();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addProvider(AuthFactory.binder(chainedAuthFactory))
        .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
        .addResource(new UserResource(userManager))
        .build();

    @Before
    public void setup() throws Exception {
        globalAdmin.setId(1L);
        when(userManager.findById(1L)).thenReturn(Optional.of(globalAdmin));
        when(userManager.refreshToken(globalAdmin)).thenReturn(new CodingyardToken("ssup", new Date()));
        BasicCredentials credentials = new BasicCredentials(globalAdmin.getUsername(), globalAdmin.getPassword());
        when(userCredentialAuthenticator.authenticate(credentials)).thenReturn(Optional.of(globalAdmin));
        when(tokenAuthenticator.authenticate(any(String.class))).thenReturn(Optional.of(globalAdmin));

    }

    @Test
    public void existingUserShouldBeFound() {
        final CodingyardUser actual = resources.getJerseyTest()
            .target("/user")
            .path(globalAdmin.getId().toString())
            .request(MediaType.APPLICATION_JSON)
            .get(CodingyardUser.class);
        assertThat(actual).isEqualTo(globalAdmin);
        verify(userManager).findById(globalAdmin.getId());
    }

    // TODO: Finish this
    @Test
    public void loginShouldRegenerateToken() {

        resources.getJerseyTest().client().register(HttpAuthenticationFeature.basic(globalAdmin.getUsername(), globalAdmin.getPassword()));
        Response response = resources.getJerseyTest()
            .target("/user")
            .path("login")
            .request()
            .post(Entity.json(""));

    }


}
