package com.codingyard.resources.solution;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.manager.UserManager;
import com.codingyard.permission.SolutionAccessApprover;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Path("/solution/topcoder")
public class TopCoderSolutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TopCoderSolutionResource.class);
    private final TopCoderSolutionManager tcManager;
    private final UserManager userManager;

    public TopCoderSolutionResource(final UserManager userManager, final TopCoderSolutionManager tcManager) {
        this.tcManager = tcManager;
        this.userManager = userManager;
    }

    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadSolution(@Auth CodingyardUser author,
                                   @FormParam("division") TopCoderDivision division,
                                   @FormParam("difficulty") TopCoderDifficulty difficulty,
                                   @FormParam("problem_number") Long problemNumber,
                                   @FormParam("language") Language language,
                                   @FormParam("content") String content) {

        if (!SolutionAccessApprover.canCreate(author)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(String.format("%s is not authorized to upload a solution.\n", author))
                .build();
        }

        try {
            final String filePath = tcManager.save(author, content, division, difficulty, problemNumber, language).toString();
            final TopCoderSolution solution = new TopCoderSolution(author, new Date(), filePath, language, difficulty, division, problemNumber);
            userManager.saveSolution(author, solution);
            userManager.flush();
            return Response.status(Response.Status.CREATED)
                .entity(solution.getId())
                .build();
        } catch (IOException e) {
            LOG.warn("Exception thrown while trying to save a topcoder solution for user : {}, division : {], difficulty : {}," +
                "problem number : {}, language : {}.", author, division, difficulty, problemNumber, language, e);
            return Response.serverError().entity("Unable to process the request currently... sorry.\n").build();
        }
    }

    @Path("/{solution_id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolution(@PathParam("solution_id") @NotNull Long solutionId) {

        final Optional<TopCoderSolution> searchResult = tcManager.findById(solutionId);
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("There is no solution with id %d.\n", solutionId))
                .build();
        }

        final TopCoderSolution solution = searchResult.get();
        return Response.ok().entity(solution).build();
    }

    @Path("/{solution_id}/content")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.TEXT_PLAIN)
    public Response getContent(@PathParam("solution_id") @NotNull Long solutionId) {
        final Optional<TopCoderSolution> searchResult = tcManager.findById(solutionId);
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("There is no solution with id %d.\n", solutionId))
                .build();
        }
        try {
            final String content = Joiner.on("\n").join(tcManager.load(searchResult.get()));
            return Response.ok().entity(content).build();
        } catch (IOException e) {
            return Response.serverError().entity("Couldn't find content.").build();
        }
    }

    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolution(@QueryParam("division") Optional<TopCoderDivision> division,
                                @QueryParam("difficulty") Optional<TopCoderDifficulty> difficulty,
                                @QueryParam("problem_number") Optional<Long> problemNumber,
                                @QueryParam("language") Optional<Language> language,
                                @QueryParam("author_username") Optional<String> username) {

        Optional<Long> userId = Optional.absent();
        if (username.isPresent()) {
            final Optional<CodingyardUser> searchResult = userManager.findByUsername(username.get());
            if (!searchResult.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("There is no user with username %s.\n", username))
                    .build();
            }
            userId = Optional.of(searchResult.get().getId());
        }

        List<TopCoderSolution> solutions = tcManager.findAll(division, difficulty, problemNumber, language, userId);

        return Response.ok().entity(solutions).build();
    }

    @Path("/{solution_id}")
    @DELETE
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSolution(@Auth CodingyardUser user,
                                   @PathParam("solution_id") @NotNull Long solutionId) {
        final Optional<TopCoderSolution> searchResult = tcManager.findById(solutionId);
        /*
        Note that I am deliberately returning 404 NOT_FOUND even when a solution exists
        but the user is not authorized to delete the solution. This is to not give out
        the existence of a solution to a user who is not authorized to delete it.
         */
        if (searchResult.isPresent() && SolutionAccessApprover.canDelete(user, searchResult.get())) {
            final boolean isDeleted = tcManager.delete(searchResult.get());
            if (isDeleted) {
                return Response.ok().build();
            } else {
                return Response.serverError().entity("Couldn't delete the solution. Please try again later.").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Couldn't find solution with id " + solutionId)
                .build();
        }
    }
}
