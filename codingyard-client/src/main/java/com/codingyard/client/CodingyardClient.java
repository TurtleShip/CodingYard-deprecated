package com.codingyard.client;

import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.api.payload.RoleChangePayload;
import com.codingyard.path.UserResourcePath;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CodingyardClient {

    private final Client client;

    public CodingyardClient(Client client) {
        this.client = client;
    }

    public Response getUser(final Long userId) {
        return client.target(UserResourcePath.findUserPath(userId))
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

        return client.target(UserResourcePath.CREATE_USER_PATH)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));
    }

    public Response createUser(final CodingyardUser user) {
        return createUser(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    public Response login(final String username, final String password) {
        client.register(HttpAuthenticationFeature.basic(username, password));
        return client.target(UserResourcePath.LOGIN_PATH)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(""));
    }

    public Response login(final CodingyardUser user) {
        return login(user.getUsername(), user.getPassword());
    }

    public Response changeRole(final String approverToken,
                               final Long targetUserId,
                               final Role newRole) {
        final RoleChangePayload request = new RoleChangePayload(targetUserId, newRole);
        return client.target(UserResourcePath.CHANGE_ROLE_PATH)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", bearerToken(approverToken))
            .put(Entity.json(request));


    }

    private String bearerToken(final String token) {
        return String.format("bearer %s", token);
    }

}
