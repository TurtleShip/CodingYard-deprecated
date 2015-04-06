package com.codingyard.client;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.api.payload.RoleChangePayload;
import com.codingyard.path.SolutionResourcePath;
import com.codingyard.path.UserResourcePath;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;

public class CodingyardClient {

    private final Client client;
    private final URL root;

    public CodingyardClient(final Client client, final URL root) {
        this.client = client;
        this.root = root;
        client.register(HttpAuthenticationFeature.basicBuilder().credentials("", "").build());
    }

    // =============================== UserResource methods ===============================
    public Response getUser(final Long userId) {

        return client.target(root.toString() + UserResourcePath.findUserPath(userId))
            .request(MediaType.APPLICATION_JSON)
            .get();
    }

    public Response createUser(final String username,
                               final String password,
                               final String firstName,
                               final String lastName) {
        final Form form = new Form()
            .param("username", username)
            .param("password", password)
            .param("firstName", firstName)
            .param("lastName", lastName);

        return client.target(root.toString() + UserResourcePath.CREATE_USER_PATH)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));
    }

    public Response createUser(final CodingyardUser user) {
        return createUser(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    public Response login(final String username, final String password) {
        return client.target(root.toString() + UserResourcePath.LOGIN_PATH)
            .request(MediaType.APPLICATION_JSON)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, username)
            .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password)
            .post(Entity.json(""));

    }

    public Response login(final CodingyardUser user) {
        return login(user.getUsername(), user.getPassword());
    }

    public Response changeRole(final String approverToken,
                               final Long targetUserId,
                               final Role newRole) {
        final RoleChangePayload request = new RoleChangePayload(targetUserId, newRole);
        return client.target(root.toString() + UserResourcePath.CHANGE_ROLE_PATH)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", bearerToken(approverToken))
            .put(Entity.json(request));


    }

    // =============================== SolutionResource methods ===============================
    public Response uploadTopCoderSolution(final String authorToken, final TopCoderSolution solution, final String content) {
        final Form form = new Form()
            .param("division", solution.getDivision().name())
            .param("difficulty", solution.getDifficulty().name())
            .param("problem_id", solution.getProblemId().toString())
            .param("language", solution.getLanguage().name())
            .param("content", content);

        return client.target(root.toString() + SolutionResourcePath.TOPCODER_PATH)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", bearerToken(authorToken))
            .post(Entity.form(form));
    }


    // =============================== Helper methods ===============================
    private String bearerToken(final String token) {
        return String.format("bearer %s", token);
    }

}
