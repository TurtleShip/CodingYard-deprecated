package com.codingyard.resources;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.api.payload.RoleChangeRequest;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.manager.UserManager;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.auth.oauth.OAuthFactory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserResourceTest {

    private static final String ROOT = "/user";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final UserManager userManager = mock(UserManager.class);
    private static final UserCredentialAuthenticator userCredentialAuthenticator = mock(UserCredentialAuthenticator.class);
    private static final TokenAuthenticator tokenAuthenticator = mock(TokenAuthenticator.class);
    private static final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
        new BasicAuthFactory<>(userCredentialAuthenticator, "Basic User Auth", CodingyardUser.class),
        new OAuthFactory<>(tokenAuthenticator, "Bearer User OAuth", CodingyardUser.class)
    );

    private CodingyardUser globalAdmin;
    private CodingyardUser admin;
    private CodingyardUser member;
    private CodingyardUser guest;
    private CodingyardUser nonExistingUser;
    private long id = 1;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
        .addProvider(AuthFactory.binder(chainedAuthFactory))
        .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
        .addResource(new UserResource(userManager))
        .build();

    @BeforeClass
    public static void setupOnce() {
        resources.getJerseyTest().client().register(HttpAuthenticationFeature.basic("", ""));
    }

    @Before
    public void setup() throws Exception {
        globalAdmin = new CodingyardUser.Builder("turtleship", "safe_password").role(Role.GLOBAL_ADMIN).build();
        admin = new CodingyardUser.Builder("admin_user", "secure_pwd").role(Role.ADMIN).build();
        member = new CodingyardUser.Builder("good_member", "cracking-code").role(Role.MEMBER).build();
        guest = new CodingyardUser.Builder("fresh-guest", "let-me-in").role(Role.GUEST).build();
        nonExistingUser = new CodingyardUser.Builder("who-am-i", "no-one").build();

        setupUser(globalAdmin);
        setupUser(admin);
        setupUser(member);
        setupUser(guest);

        setupNonExistingUser(nonExistingUser);
    }

    private void setupUser(final CodingyardUser user) throws AuthenticationException {
        user.setId(id++);
        user.setToken(CodingyardToken.Builder.build());
        mockUser(user);
    }

    private void mockUser(final CodingyardUser user) throws AuthenticationException {
        when(userManager.findById(user.getId())).thenReturn(Optional.of(user));
        when(userManager.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userCredentialAuthenticator.authenticate(new BasicCredentials(user.getUsername(), user.getPassword()))).thenReturn(Optional.of(user));
        when(tokenAuthenticator.authenticate(user.getToken().getValue())).thenReturn(Optional.of(user));
    }

    private void setupNonExistingUser(final CodingyardUser user) throws AuthenticationException {
        user.setId(0L);
        user.setToken(CodingyardToken.Builder.build());
        when(userManager.findById(user.getId())).thenReturn(Optional.absent());
        when(userManager.findByUsername(user.getUsername())).thenReturn(Optional.absent());
        when(userCredentialAuthenticator.authenticate(new BasicCredentials(user.getUsername(), user.getPassword()))).thenReturn(Optional.absent());
        when(tokenAuthenticator.authenticate(user.getToken().getValue())).thenReturn(Optional.absent());
    }

    @Test
    public void findUserShouldReturnExistingUsers() {
        final CodingyardUser actual = resources.getJerseyTest()
            .target(ROOT)
            .path(globalAdmin.getId().toString())
            .request(MediaType.APPLICATION_JSON)
            .get(CodingyardUser.class);
        assertThat(actual).isEqualTo(globalAdmin);
        verify(userManager).findById(globalAdmin.getId());
    }

    @Test
    public void findUserShouldReturn404ForNonExistingUsers() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path(nonExistingUser.getId().toString())
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getSolutionsShouldReturnExistingSolutions() {
        TopCoderSolution solution = new TopCoderSolution(member, new Date(),
            Language.CPP, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 255L);

        member.setSolutions(Sets.newHashSet(solution));

        Set<TopCoderSolution> actual = resources.getJerseyTest()
            .target(ROOT)
            .path(member.getId().toString())
            .path("solutions")
            .request(MediaType.APPLICATION_JSON)
            .get(new GenericType<Set<TopCoderSolution>>() {
            });

        assertThat(actual).isEqualTo(member.getSolutions());

    }

    @Test
    public void getSolutionsForNonExistingUsersShouldReturn404() {
        final Response response = resources.getJerseyTest().target(ROOT)
            .path(nonExistingUser.getId().toString())
            .path("solutions")
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void findMyIdShouldReturnExistingUserId() {
        final Long id = resources.getJerseyTest().target(ROOT)
            .path("id")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, member.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, member.getPassword())
            .get(Long.class);

        assertThat(id).isEqualTo(member.getId());
    }

    @Test
    public void findMyIdShouldReturnUnauthorizedForNonExistingUsers() {
        final Response response = resources.getJerseyTest().target(ROOT)
            .path("id")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, nonExistingUser.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, nonExistingUser.getPassword())
            .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void createUserShouldCreateAGuestUser() {
        final CodingyardUser newUser = new CodingyardUser.Builder("I am so", "fresh")
            .firstName("Wu")
            .lastName("Kong")
            .role(Role.GUEST)
            .build();

        when(userManager.save(newUser)).thenReturn(-1L);

        final Form form = new Form()
            .param("username", newUser.getUsername())
            .param("password", newUser.getPassword())
            .param("firstName", newUser.getFirstName())
            .param("lastName", newUser.getLastName());

        resources.getJerseyTest().target(ROOT)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));

        verify(userManager).save(newUser);
    }

    // TODO: Create a test to test login AFTER making fixes for login endpoint

    @Test
    public void adminCanPromoteGuestToMember() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("role")
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(admin.getToken().getValue()))
            .put(Entity.json(new RoleChangeRequest(guest.getId(), Role.MEMBER)));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(guest.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    public void memberCannotPromoteGuestToAdmin() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("role")
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(member.getToken().getValue()))
            .put(Entity.json(new RoleChangeRequest(guest.getId(), Role.ADMIN)));

        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
        assertThat(guest.getRole()).isEqualTo(Role.GUEST);
    }

    @Test
    public void changeRoleShouldReturnUnauthorizedForNonExistingUsers() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("role")
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(nonExistingUser.getToken().getValue()))
            .put(Entity.json(new RoleChangeRequest(guest.getId(), Role.MEMBER)));

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    private String bearerToken(final String token) {
        return String.format("bearer %s", token);
    }
}
