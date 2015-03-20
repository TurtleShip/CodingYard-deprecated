package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.dao.UserDAO;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.entity.user.Role;
import com.codingyard.util.Encryptor;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Path("/{id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public CodingyardUser findUser(@PathParam("id") LongParam id) {
        return userDAO.findById(id.get());
    }

    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Long createUser(@FormParam("username") @NotNull final String username,
                           @FormParam("password") @NotNull final String password,
                           @FormParam("firstName") @NotNull final String firstName,
                           @FormParam("lastName") @NotNull final String lastName,
                           @FormParam("role") @NotNull final Role role) {
        final CodingyardUser codingyardUser = new CodingyardUser(username, Encryptor.encrypt(password), firstName, lastName, role);
        return userDAO.save(codingyardUser);
    }

    // TODO: Add an endpoint to change user's role. Make sure that authorizer has a proper permission to change
    // another user's role
}
