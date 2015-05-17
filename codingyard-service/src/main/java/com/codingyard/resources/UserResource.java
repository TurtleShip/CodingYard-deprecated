package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.payload.RoleChangeRequest;
import com.codingyard.api.payload.TokenAndUser;
import com.codingyard.manager.UserManager;
import com.codingyard.util.UserRoleApprover;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    private final UserManager userManager;

    public UserResource(UserManager userManager) {
        this.userManager = userManager;
    }

    @Path("/{id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(@PathParam("id") LongParam id) {
        Optional<CodingyardUser> searchResult = userManager.findById(id.get());
        if (searchResult.isPresent()) {
            return Response.ok(searchResult.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @Path("/{id}/solutions")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolutions(@PathParam("id") LongParam userId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(userId.get());
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final CodingyardUser author = searchResult.get();
        return Response.ok().entity(author.getSolutions()).build();
    }

    @Path("/id")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMyId(@Auth CodingyardUser user) {
        return Response.ok().entity(user.getId()).build();
    }

    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Long createUser(@FormParam("username") @NotNull final String username,
                           @FormParam("password") @NotNull final String password,
                           @FormParam("firstName") @NotNull final String firstName,
                           @FormParam("lastName") @NotNull final String lastName) {
        final CodingyardUser codingyardUser = new CodingyardUser.Builder(username, password)
            .firstName(firstName)
            .lastName(lastName)
            .build();

        return userManager.save(codingyardUser);
    }

    @Path("/login")
    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Auth CodingyardUser user) {
        userManager.refreshToken(user);
        final TokenAndUser userInfo = new TokenAndUser(user.getToken().getValue(), user);
        return Response.status(Response.Status.OK)
            .entity(userInfo)
            .build();
    }

    @Path("/role")
    @PUT
    @Metered
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeRole(@Auth CodingyardUser approver,
                               @Valid RoleChangeRequest request) {

        final Long lowerUserId = request.getUserId();
        final Optional<CodingyardUser> searchResult = userManager.findById(lowerUserId);
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("User with id %d was not found.\n", lowerUserId))
                .build();
        }
        final CodingyardUser lowerUser = searchResult.get();

        final boolean isApproved = UserRoleApprover.approve(approver, lowerUser, request.getNewRole());

        if (isApproved) {
            return Response.ok()
                .entity(String.format("User %s's role change to %s was successfully approved by user %s.\n", lowerUser.getUsername(), lowerUser.getRole(), approver.getUsername()))
                .build();
        } else {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(String.format("User %s does not have permission to change user %s's role to %s.\n", approver.getUsername(), lowerUser.getUsername(), request.getNewRole()))
                .build();
        }
    }
}
