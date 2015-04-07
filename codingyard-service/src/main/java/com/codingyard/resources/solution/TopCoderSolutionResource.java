package com.codingyard.resources.solution;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.manager.TopCoderSolutionManager;
import com.codingyard.manager.UserManager;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                                   @FormParam("problem_id") Long problemId,
                                   @FormParam("language") Language language,
                                   @FormParam("content") String content) {

        if (author.getRole().getLevel() == Role.GUEST.getLevel()) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity("Guest cannot upload a solution.\n")
                .build();
        }

        try {
            final String filePath = tcManager.save(author, content, division, difficulty, problemId, language).toString();
            final TopCoderSolution solution = new TopCoderSolution(author, new Date(), filePath, language, difficulty, division, problemId);
            userManager.saveSolution(author, solution);

            return Response.status(Response.Status.CREATED)
                .entity(solution.getProblemId())
                .build();
        } catch (IOException e) {
            LOG.warn("Exception thrown while trying to save a topcoder solution for user : {}, division : {], difficulty : {}," +
                "problem id : {}, language : {}.", author, division, difficulty, problemId, language, e);
            return Response.serverError().entity("Unable to process the request currently... sorry.\n").build();
        }
    }

    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopCoderSolution(@QueryParam("division") TopCoderDivision division,
                                        @QueryParam("difficulty") TopCoderDifficulty difficulty,
                                        @QueryParam("problem_id") Long problemId,
                                        @QueryParam("language") Language language,
                                        @QueryParam("author_username") String username) {

        final Optional<CodingyardUser> searchResult = userManager.findByUsername(username);
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("There is no user with username %s.\n", username))
                .build();
        }

        final CodingyardUser author = searchResult.get();

        try {
            final List<String> content = tcManager.load(author, division, difficulty, problemId, language);
            return Response.ok().entity(content).build();

        } catch (IOException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("User %s doesn't have the requested solution.\n", username))
                .build();
        }
    }

    @Path("/{solution_id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopCoderSolutionById(@PathParam("solution_id") Long solutionId) {

        final Optional<TopCoderSolution> searchResult = tcManager.findById(solutionId);
        if (!searchResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(String.format("There is no solution with id %d.\n", solutionId))
                .build();
        }

        final TopCoderSolution solution = searchResult.get();

        try {
            final List<String> content = tcManager.load(solution);
            return Response.ok().entity(content).build();

        } catch (IOException e) {
            LOG.warn("Couldn't find content for the solution with {}.", solutionId, e);
            return Response.serverError()
                .entity(String.format("Oops... We are having trouble finding content for solution with id %d.\n", solutionId))
                .build();
        }
    }

}
