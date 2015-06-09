package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.payload.RoleChangeRequest;
import com.codingyard.manager.UserManager;
import com.codingyard.permission.UserRoleApprover;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

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
            return Response.status(NOT_FOUND).build();
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
            return Response.status(NOT_FOUND).build();
        }
        final CodingyardUser author = searchResult.get();
        return Response.ok().entity(author.getSolutions()).build();
    }

    @Path("/me")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyInfo(@Auth CodingyardUser user) {
        return Response.ok().entity(user).build();
    }

    @Path("/me/edit")
    @PUT
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMyInfo(@Auth CodingyardUser currentUser,
                               @Valid CodingyardUser newUser) {
        // A user can only edit his/her own info
        if (currentUser.getId().longValue() != newUser.getId().longValue()) {
            return Response.status(FORBIDDEN).entity("You are not allowed to edit user " + newUser).build();
        }

        /*
         Allowed changes:
            firstName
            lastName
            password

         Not allowed changes:
            username
            role
          */
        if (!currentUser.getUsername().equals(newUser.getUsername())) {
            return Response.status(FORBIDDEN).entity("You can't change your username").build();
        }
        if (!currentUser.getRole().equals(newUser.getRole())) {
            return Response.status(FORBIDDEN).entity("You can't change your role").build();
        }

        userManager.save(newUser);
        return Response.ok().entity(newUser).build();
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
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@Auth CodingyardUser user) {
        return Response.ok().entity(user.getToken().getValue()).build();
    }

    @Path("/token/refresh")
    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(@Auth final CodingyardUser user) {
        userManager.refreshToken(user);
        return Response.ok().entity(user.getToken()).build();
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
            return Response.status(NOT_FOUND)
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
            return Response.status(FORBIDDEN)
                .entity(String.format("User %s does not have permission to change user %s's role to %s.\n", approver.getUsername(), lowerUser.getUsername(), request.getNewRole()))
                .build();
        }
    }
}
