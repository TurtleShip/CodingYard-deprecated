package com.codingyard.resources.permission;

import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.manager.UserManager;
import com.codingyard.permission.UserAccessApprover;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Path("/edit")
    @GET
    @UnitOfWork
    public Response canEdit(@Auth final CodingyardUser user,
                            @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return Response.ok().entity(UserAccessApprover.canEdit(user, searchResult.get())).build();
    }

    @Path("/edit/role")
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditableRoles(@Auth final CodingyardUser approver,
                                     @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return Response.ok().entity(UserAccessApprover.getEditableRoles(approver, searchResult.get())).build();
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

        return Response.ok().entity(UserAccessApprover.canDelete(me, searchResult.get())).build();
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

        return Response.ok().entity(UserAccessApprover.canEditPassword(me, searchResult.get())).build();
    }

    @Path("/edit/firstname")
    @GET
    @UnitOfWork
    public Response canEditFirstName(@Auth final CodingyardUser me,
                                     @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return Response.ok().entity(UserAccessApprover.canEditFirstName(me, searchResult.get())).build();
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

        return Response.ok().entity(UserAccessApprover.canEditLastName(me, searchResult.get())).build();
    }

    @Path("/edit/email")
    @GET
    @UnitOfWork
    public Response canEditEmail(@Auth final CodingyardUser me,
                                 @PathParam("user_id") final long targetUserId) {
        final Optional<CodingyardUser> searchResult = userManager.findById(targetUserId);

        if (!searchResult.isPresent()) {
            return createNotFoundResponse(targetUserId);
        }

        return Response.ok().entity(UserAccessApprover.canEditEmail(me, searchResult.get())).build();
    }

    private Response createNotFoundResponse(final long userId) {
        return Response.status(NOT_FOUND)
            .entity(String.format("user with id %d is not found.", userId))
            .build();
    }
}
