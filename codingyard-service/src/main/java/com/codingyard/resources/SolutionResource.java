package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.dao.UserDAO;
import com.codingyard.manager.TopCoderSolutionManager;
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

@Path("/solution")
public class SolutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SolutionResource.class);
    private final TopCoderSolutionManager tcManager;
    private final UserDAO userDAO;

    public SolutionResource(UserDAO userDAO, TopCoderSolutionManager tcManager) {
        this.userDAO = userDAO;
        this.tcManager = tcManager;
    }

    @Path("/topcoder")
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
            author.getSolutions().add(solution);
            solution.setAuthor(author);

            userDAO.save(author);

            return Response.status(Response.Status.CREATED)
                .entity(solution.getProblemId())
                .build();
        } catch (IOException e) {
            LOG.warn("Exception thrown while trying to save a topcoder solution for user : {}, division : {], difficulty : {}," +
                "problem id : {}, language : {}.", author, division, difficulty, problemId, language, e);
            return Response.serverError().entity("Unable to process the request currently... sorry.\n").build();
        }
    }

    @Path("/topcoder")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolution(@QueryParam("division") TopCoderDivision division,
                                @QueryParam("difficulty") TopCoderDifficulty difficulty,
                                @QueryParam("problem_id") Long problemId,
                                @QueryParam("language") Language language,
                                @QueryParam("author_username") String username) {

        final Optional<CodingyardUser> searchResult = userDAO.findByUsername(username);
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

    @Path("/topcoder/{solution_id}")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolutionById(@PathParam("solution_id") Long solutionId) {

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
