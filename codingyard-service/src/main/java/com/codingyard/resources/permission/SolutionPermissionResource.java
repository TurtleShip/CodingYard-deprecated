package com.codingyard.resources.permission;

import com.codingyard.api.entity.contest.Contest;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.permission.SolutionAccessApprover;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Resource endpoint for checking if a user has permission do a certain operation
 * on a solution.
 */
@Path("/permission/solution")
public class SolutionPermissionResource {

    private final TopCoderSolutionManager tcoSolutionManager;

    public SolutionPermissionResource(TopCoderSolutionManager tcoSolutionManager) {
        this.tcoSolutionManager = tcoSolutionManager;
    }

    @GET
    @Path("/delete/{contest}/{solution_id}")
    @UnitOfWork
    public Response canDelete(@Auth CodingyardUser user,
                              @PathParam("contest") Contest contest,
                              @PathParam("solution_id") long solutionId) {
        final Optional<Solution> searchResult = findSolution(contest, solutionId);
        if (searchResult.isPresent()) {
            return createAccessResponse(SolutionAccessApprover.canDelete(user, searchResult.get()));
        } else {
            return createNotFoundResponse(solutionId);
        }
    }

    @GET
    @Path("/edit/{contest}/{solution_id}")
    @UnitOfWork
    public Response canEdit(@Auth CodingyardUser user,
                            @PathParam("contest") Contest contest,
                            @PathParam("solution_id") long solutionId) {
        final Optional<Solution> searchResult = findSolution(contest, solutionId);
        if (searchResult.isPresent()) {
            return createAccessResponse(SolutionAccessApprover.canEdit(user, searchResult.get()));
        } else {
            return createNotFoundResponse(solutionId);
        }
    }

    @GET
    @Path("/create")
    @UnitOfWork
    public Response canCreate(@Auth CodingyardUser user) {
        return createAccessResponse(SolutionAccessApprover.canCreate(user));
    }

    private Optional<Solution> findSolution(final Contest contest,
                                            final long solutionId) {
        switch (contest) {
            case TOP_CODER:
                final Optional<TopCoderSolution> searchResult = tcoSolutionManager.findById(solutionId);
                return searchResult.isPresent() ? Optional.of(searchResult.get()) : Optional.absent();
            default:
                throw new IllegalArgumentException("Contest of type " + contest + " is not supported");
        }
    }


    private Response createNotFoundResponse(final long solutionId) {
        return Response.status(NOT_FOUND)
            .entity(String.format("Solution with id %d is not found.", solutionId))
            .build();
    }

    private Response createAccessResponse(final boolean isAllowed) {
        return Response.ok(isAllowed).build();
    }
}
