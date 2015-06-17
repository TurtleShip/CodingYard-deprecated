package com.codingyard.resources.permission;

import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.manager.UserManager;
import com.codingyard.permission.UserAccessApprover;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Resource endpoint for checking if a user has permission do a certain operation
 * on another user.
 */
@Path("/permission/user/{user_id}")
public class UserPermissionResource {

    private final UserManager userManager;

    public UserPermissionResource(UserManager userManager) {
        this.userManager = userManager;
    }

    @Path("/role")
    @GET
    @UnitOfWork
    public Response canEditRole(@Auth final CodingyardUser approver,
                                @PathParam("user_id") final long targetUserId,
                                @QueryParam("role") final Role role) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return createAccessResponse(UserAccessApprover.canEditRole(approver, searchResult.get(), role));
    }

    @Path("/delete")
    @GET
    @UnitOfWork
    public Response canDelete(@Auth final CodingyardUser me,
                              @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return createAccessResponse(UserAccessApprover.canDelete(me, searchResult.get()));
    }

    @Path("/edit/password")
    @GET
    @UnitOfWork
    public Response canEditPassword(@Auth final CodingyardUser me,
                                    @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return createAccessResponse(UserAccessApprover.canEditPassword(me, searchResult.get()));
    }

    @Path("/edit/firstname")
    @GET
    @UnitOfWork
    public Response canFirstName(@Auth final CodingyardUser me,
                                 @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return createAccessResponse(UserAccessApprover.canEditFirstName(me, searchResult.get()));
    }

    @Path("/edit/lastname")
    @GET
    @UnitOfWork
    public Response canEditLastName(@Auth final CodingyardUser me,
                                    @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return createAccessResponse(UserAccessApprover.canEditLastName(me, searchResult.get()));
    }

    private Response createNotFoundResponse(final long userId) {
        return Response.status(NOT_FOUND)
            .entity(String.format("user with id %d is not found.", userId))
            .build();
    }

    private Response createAccessResponse(final boolean isAllowed) {
        if (isAllowed) {
            return Response.ok().build();
        } else {
            return Response.status(FORBIDDEN).build();
        }
    }
}
