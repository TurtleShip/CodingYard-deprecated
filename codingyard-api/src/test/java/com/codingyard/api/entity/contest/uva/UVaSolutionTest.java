package com.codingyard.api.entity.contest.uva;

import com.codingyard.api.BasicJsonTest;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import org.junit.Before;

import java.util.Date;

public class UVaSolutionTest extends BasicJsonTest<UVaSolution> {

    final CodingyardUser author = new CodingyardUser.Builder("TurtleShip", "safe_password")
        .firstName("Seulgi")
        .lastName("Kim")
        .role(Role.GLOBAL_ADMIN)
        .email("test@codingyard.com")
        .build();
    final Date submissionDate = new Date(1234L);

    final UVaSolution solution = new UVaSolution(author, submissionDate, "secure_path", Language.JAVA, 123L);

    @Before
    public void setUp() {
        author.setId(1L);
        solution.setId(999L);
    }

    @Override
    public String getJsonFilePath() {
        return "fixtures/solution/uva_solution.json";
    }

    @Override
    public UVaSolution getValidPojo() {
        return solution;
    }

    @Override
    public Class<? extends UVaSolution> getEntityClass() {
        return UVaSolution.class;
    }
}
