package com.codingyard.entity.contest.topcoder;

import com.codingyard.entity.contest.Contest;
import com.codingyard.entity.contest.Solution;
import com.codingyard.entity.user.User;

import java.nio.file.Path;
import java.util.Date;

public class TopCoderSolution extends Solution {

    private final TopCoderDifficulty difficulty;
    private final TopCoderDivision division;

    public TopCoderSolution(final Contest contest,
                            final User author,
                            final Date submissionDate,
                            final Path filePath,
                            final TopCoderDifficulty difficulty,
                            final TopCoderDivision division) {
        super(contest, author, submissionDate, filePath);
        this.difficulty = difficulty;
        this.division = division;
    }

    public TopCoderDifficulty getDifficulty() {
        return difficulty;
    }

    public TopCoderDivision getDivision() {
        return division;
    }
}
