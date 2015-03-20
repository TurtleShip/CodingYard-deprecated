package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.dao.UserDAO;
import com.codingyard.entity.user.Role;
import com.codingyard.entity.user.CodingyardUser;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

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
    public CodingyardUser findUser(@PathParam("id") LongParam id) {
        return userDAO.findById(id.get());
    }

    @POST
    @Metered
    @UnitOfWork
    public Long createUser(@FormParam("username") @NotNull final String username,
                           @FormParam("password") @NotNull final String password,
                           @FormParam("firstName") @NotNull final String firstName,
                           @FormParam("lastName") @NotNull final String lastName,
                           @FormParam("role") @NotNull final Role role) {
        final CodingyardUser codingyardUser = new CodingyardUser(username, password, firstName, lastName, role);
        return userDAO.save(codingyardUser);
    }
}
