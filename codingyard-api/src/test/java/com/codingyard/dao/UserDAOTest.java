package com.codingyard.dao;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class UserDAOTest extends H2Test {

    private final UserDAO userDAO = new UserDAO(sessionFactory);

    @Test
    public void userShouldBeSavedAndRetrieved() {
        final String username = "TurtleShip";
        final CodingyardUser user = new CodingyardUser.Builder(username, "secure_password").build();
        final Long id = userDAO.save(user);
        final Optional<CodingyardUser> searchResultById = userDAO.findById(id);
        final Optional<CodingyardUser> searchResultByUsername = userDAO.findByUsername(username);

        assertTrue(searchResultById.isPresent());
        assertTrue(searchResultByUsername.isPresent());
        assertThat(searchResultById.get()).isEqualTo(user);
        assertThat(searchResultByUsername.get()).isEqualTo(user);
    }

    @Test
    public void userTokenShouldBeRetrieved() {
        final CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "secure_password").build();
        final CodingyardToken token = user.getToken();

        final Long id = userDAO.save(user);
        final Optional<CodingyardUser> searchResult = userDAO.findById(id);

        assertTrue(searchResult.isPresent());
        assertThat(searchResult.get().getToken()).isEqualTo(token);
    }

    @Test
    public void userSolutionShouldBeRetrieved() {
        final CodingyardUser user = new CodingyardUser.Builder("TurtleShip", "secure_password").build();
        final TopCoderSolution solutionOne =
            new TopCoderSolution(user, new Date(), Language.CPP, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 255L);
        final TopCoderSolution solutionTwo =
            new TopCoderSolution(user, new Date(), Language.JAVA, TopCoderDifficulty.EASY, TopCoderDivision.DIV2, 255L);
        user.getSolutions().add(solutionOne);
        user.getSolutions().add(solutionTwo);

        final Long id = userDAO.save(user);
        final Optional<CodingyardUser> searchResult = userDAO.findById(id);

        assertTrue(searchResult.isPresent());
        assertThat(searchResult.get().getSolutions().size()).isEqualTo(2);
        assertTrue(searchResult.get().getSolutions().contains(solutionOne));
        assertTrue(searchResult.get().getSolutions().contains(solutionTwo));
    }

}
