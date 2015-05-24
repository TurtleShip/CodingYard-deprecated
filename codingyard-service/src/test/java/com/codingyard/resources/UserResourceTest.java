package com.codingyard.resources;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.api.payload.RoleChangeRequest;
import com.google.common.collect.Sets;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
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

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserResourceTest extends ResourceTest {

    private static final String ROOT = "/user";

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
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void userCannotChangeOtherUserInfo() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("edit")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, admin.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, admin.getPassword())
            .put(Entity.json(member));
        assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
    }

    @Test
    public void userCannotChangeUsername() {
        final CodingyardUser newAdmin = new CodingyardUser(admin);
        newAdmin.setUsername("Cool new name");
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("edit")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, admin.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, admin.getPassword())
            .put(Entity.json(newAdmin));
        assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
    }


    // Note the careful test naming. Users **can** change their roles through different endpoint : /user/role
    @Test
    public void userCannotChangeRoleThroughUserEdit() {
        final CodingyardUser newAdmin = new CodingyardUser(admin);
        newAdmin.setRole(Role.GLOBAL_ADMIN);
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("edit")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, admin.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, admin.getPassword())
            .put(Entity.json(newAdmin));
        assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
    }

    @Test
    public void userCanEditNamesAndPassword() {
        final CodingyardUser newAdmin = new CodingyardUser(admin);
        newAdmin.setFirstName("new first name");
        newAdmin.setLastName("new last name");
        newAdmin.setPassword("new and secure password");

        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("edit")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, admin.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, admin.getPassword())
            .put(Entity.json(newAdmin));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
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

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void findMyInfoShouldReturnExistingUser() {
        final CodingyardUser actual = resources.getJerseyTest().target(ROOT)
            .path("me")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, member.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, member.getPassword())
            .get(CodingyardUser.class);

        assertThat(actual).isEqualTo(member);
    }

    @Test
    public void findMyInfoShouldReturnUnauthorizedForNonExistingUsers() {
        final Response response = resources.getJerseyTest().target(ROOT)
            .path("me")
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, nonExistingUser.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, nonExistingUser.getPassword())
            .get();

        assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
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

    @Test
    public void loginShouldReturnTokenForExistingUsers() {
        final String actual = resources.getJerseyTest()
            .target(ROOT)
            .path("login")
            .request()
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, member.getUsername())
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, member.getPassword())
            .get(String.class);

        assertThat(actual).isEqualTo(member.getToken().getValue());
    }

    @Test
    public void loginShouldReturnUnauthorizedForNonExistingUsers() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("login")
            .request()
            .get();

        assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void refreshTokenShouldRefreshToken() {
        final String originalToken = member.getToken().getValue();
        final String newToken = resources.getJerseyTest()
            .target(ROOT)
            .path("token/refresh")
            .request()
            .post(Entity.json(""))
            .readEntity(String.class);

        assertThat(newToken).isNotEqualTo(originalToken);
    }

    @Test
    public void adminCanPromoteGuestToMember() {
        final Response response = resources.getJerseyTest()
            .target(ROOT)
            .path("role")
            .request(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, bearerToken(admin.getToken().getValue()))
            .put(Entity.json(new RoleChangeRequest(guest.getId(), Role.MEMBER)));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
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

        assertThat(response.getStatus()).isEqualTo(FORBIDDEN.getStatusCode());
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

        assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
    }
}
