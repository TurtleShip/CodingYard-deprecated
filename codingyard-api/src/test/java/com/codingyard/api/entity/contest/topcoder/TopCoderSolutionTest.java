package com.codingyard.api.entity.contest.topcoder;

import com.codingyard.api.entity.EntityTestUtil;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertTrue;

public class TopCoderSolutionTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    final CodingyardUser author = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .build();
    final Date submissionDate = new Date(1234L);
    final TopCoderSolution solution = new TopCoderSolution(author, submissionDate, "secure_path",
        Language.JAVA, TopCoderDifficulty.HARD, TopCoderDivision.DIV1, 256L);

    @Before
    public void setUp() {
        author.setUserId(1L);
        solution.setSolutionId(999L);
    }

    @Test
    public void serializesToJSON() throws Exception {
        final String actual = MAPPER.writeValueAsString(solution);
        final String expected = fixture("fixtures/solution/topcoder_solution.json");
        System.out.println(actual);
        assertTrue(EntityTestUtil.isSameInJson(actual, expected));
    }

    @Test
    public void deserializeFromJson() throws Exception {
        final TopCoderSolution actual = MAPPER.readValue(fixture("fixtures/solution/topcoder_solution.json"), TopCoderSolution.class);
        assertTrue(solution.equals(actual));
    }
}
