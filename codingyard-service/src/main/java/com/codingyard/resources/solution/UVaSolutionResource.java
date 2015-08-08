package com.codingyard.resources.solution;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.uva.UVaSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.manager.UVaSolutionManager;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

@Path("/solution/uva")
public class UVaSolutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UVaSolutionResource.class);
    private final UVaSolutionManager uvaManager;
    private final UserManager userManager;

    public UVaSolutionResource(UserManager userManager, UVaSolutionManager uvaManager) {
        this.userManager = userManager;
        this.uvaManager = uvaManager;
    }

    @POST
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadSolution(@Auth CodingyardUser author,
                                   @FormParam("problem_number") @NotNull Long problemNumber,
                                   @FormParam("language") @NotNull Language language,
                                   @FormParam("content") @NotNull String content,
                                   @FormParam("problem_name") Optional<String> problemName,
                                   @FormParam("problem_link") Optional<String> problemLink) {

        if (!SolutionAccessApprover.canCreate(author)) {
            return Response.status(FORBIDDEN)
                .entity(String.format("%s is not authorized to upload a solution.\n", author))
                .build();
        }

        try {
            final Date submissionDate = new Date();
            final String filePath = uvaManager.save(author, content, problemNumber, language, submissionDate).toString();
            final UVaSolution solution = new UVaSolution(author, submissionDate, filePath, language, problemNumber);
            if (problemName.isPresent()) {
                solution.setProblemName(problemName.get());
            }
            if (problemLink.isPresent()) {
                solution.setProblemLink(problemLink.get());
            }
            userManager.saveSolution(author, solution);
            userManager.flush();
            return Response.status(CREATED)
                .entity(solution.getId())
                .build();
        } catch (IOException e) {
            LOG.warn("Exception thrown while trying to save a UVa solution for user : {}, problem number : {}, language : {}.",
                author, problemNumber, language, e);
            return Response.serverError().entity("Unable to process the request currently... sorry.\n").build();
        }
    }

    @Path("/{solution_id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolution(@PathParam("solution_id") @NotNull Long solutionId) {

        final Optional<UVaSolution> searchResult = uvaManager.findById(solutionId);
        if (!searchResult.isPresent()) {
            return Response.status(NOT_FOUND)
                .entity(String.format("There is no solution with id %d.\n", solutionId))
                .build();
        }

        final UVaSolution solution = searchResult.get();
        return Response.ok().entity(solution).build();
    }

    @Path("/{solution_id}/content")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.TEXT_PLAIN)
    public Response getContent(@PathParam("solution_id") @NotNull Long solutionId) {
        final Optional<UVaSolution> searchResult = uvaManager.findById(solutionId);
        if (!searchResult.isPresent()) {
            return Response.status(NOT_FOUND)
                .entity(String.format("There is no solution with id %d.\n", solutionId))
                .build();
        }
        try {
            final String content = Joiner.on("\n").join(uvaManager.load(searchResult.get()));
            return Response.ok().entity(content).build();
        } catch (IOException e) {
            return Response.serverError().entity("Couldn't find content.").build();
        }
    }

    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolution(@QueryParam("problem_number") Optional<Long> problemNumber,
                                @QueryParam("language") Optional<Language> language,
                                @QueryParam("author_username") Optional<String> username) {

        Optional<Long> userId = Optional.absent();
        if (username.isPresent()) {
            final Optional<CodingyardUser> searchResult = userManager.findByUsername(username.get());
            if (!searchResult.isPresent()) {
                return Response.status(NOT_FOUND)
                    .entity(String.format("There is no user with username %s.\n", username))
                    .build();
            }
            userId = Optional.of(searchResult.get().getId());
        }

        List<UVaSolution> solutions = uvaManager.findAll(problemNumber, language, userId);

        return Response.ok().entity(solutions).build();
    }

    @Path("/{solution_id}")
    @DELETE
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSolution(@Auth CodingyardUser user,
                                   @PathParam("solution_id") @NotNull Long solutionId) {
        final Optional<UVaSolution> searchResult = uvaManager.findById(solutionId);
        /*
        Note that I am deliberately returning 404 NOT_FOUND even when a solution exists
        but the user is not authorized to delete the solution. This is to not give out
        the existence of a solution to a user who is not authorized to delete it.
         */
        if (searchResult.isPresent() && SolutionAccessApprover.canDelete(user, searchResult.get())) {

            final UVaSolution solution = searchResult.get();
            final CodingyardUser author = solution.getAuthor();

            try {
                Files.deleteIfExists(Paths.get(solution.getFilePath()));
            } catch (IOException e) {
                LOG.warn("Unable to find solution {} locally. Skipping content deletion.", solution);
            }
            final boolean isDeleted = author.getSolutions().remove(solution) && uvaManager.delete(solution);

            if (isDeleted) {
                return Response.ok().build();
            } else {
                return Response.serverError().entity("Couldn't delete the solution. Please try again later.").build();
            }
        } else {
            return Response.status(NOT_FOUND)
                .entity("Couldn't find solution with id " + solutionId)
                .build();
        }
    }

    @Path("/edit/name")
    @PUT
    @UnitOfWork
    public Response editProblemName(@Auth CodingyardUser user,
                                    @FormParam("solution_id") @NotNull Long solutionId,
                                    @FormParam("problem_name") String problemName) {
        final Optional<UVaSolution> searchResult = uvaManager.findById(solutionId);
        if (searchResult.isPresent() && SolutionAccessApprover.canEdit(user, searchResult.get())) {
            final UVaSolution solution = searchResult.get();
            solution.setProblemName(problemName);
            uvaManager.save(solution);
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND)
                .entity("Couldn't find solution with id " + solutionId)
                .build();
        }
    }

    @Path("/edit/link")
    @PUT
    @UnitOfWork
    public Response editProblemURL(@Auth final CodingyardUser user,
                                   @FormParam("solution_id") @NotNull final Long solutionId,
                                   @FormParam("problem_link") final String problemLink) {
        final Optional<UVaSolution> searchResult = uvaManager.findById(solutionId);
        if (searchResult.isPresent() && SolutionAccessApprover.canEdit(user, searchResult.get())) {
            final UVaSolution solution = searchResult.get();
            solution.setProblemLink(problemLink);
            uvaManager.save(solution);
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND)
                .entity("Couldn't find solution with id " + solutionId)
                .build();
        }
    }

}
