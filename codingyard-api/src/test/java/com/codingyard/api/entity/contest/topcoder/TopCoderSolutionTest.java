package com.codingyard.api.entity.contest.topcoder;

import com.codingyard.api.BasicJsonTest;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import org.junit.Before;

import java.util.Date;

public class TopCoderSolutionTest extends BasicJsonTest<TopCoderSolution> {

    final CodingyardUser author = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .email("test@codingyard.com")
        .build();
    final Date submissionDate = new Date(1234L);
    final TopCoderSolution solution = new TopCoderSolution(author, submissionDate, "secure_path",
        Language.JAVA, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 256L);

    @Before
    public void setUp() {
        author.setId(1L);
        solution.setId(999L);
    }

    @Override
    public String getJsonFilePath() {
        return "fixtures/solution/topcoder_solution.json";
    }

    @Override
    public TopCoderSolution getValidPojo() {
        return solution;
    }

    @Override
    public Class<? extends TopCoderSolution> getEntityClass() {
        return TopCoderSolution.class;
    }
}
