package com.codingyard.resources;

import com.codahale.metrics.annotation.Metered;
import com.codingyard.dao.UserDAO;
import com.codingyard.entity.contest.Language;
import com.codingyard.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.entity.user.CodingyardUser;
import com.codingyard.entity.user.Role;
import com.codingyard.manager.TopCoderSolutionManager;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Path("/solution")
public class SolutionResource {

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
                                   @FormParam("content") List<String> content) {

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
            return Response.serverError().entity("Unable to process the request currently... sorry.\n").build();
        }
    }

    @Path("/topcoder")
    @GET
    @Metered
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadSolution(@QueryParam("division") TopCoderDivision division,
                                     @QueryParam("difficulty") TopCoderDifficulty difficulty,
                                     @QueryParam("problem_id") Long problemId,
                                     @QueryParam("language") Language language,
                                     @QueryParam("author_id") Long authorId) {
        // TODO: Implement me
        return Response.ok().build();
    }

}
