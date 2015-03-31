package com.codingyard.entity.contest.topcoder;

import com.codingyard.entity.contest.Contest;
import com.codingyard.entity.contest.Solution;
import com.codingyard.entity.user.CodingyardUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "topcoder_solution")
public class TopCoderSolution extends Solution {

    private TopCoderDifficulty difficulty;
    private TopCoderDivision division;

    public TopCoderSolution(final CodingyardUser author,
                            final Date submissionDate,
                            final TopCoderDifficulty difficulty,
                            final TopCoderDivision division) {
        super(Contest.TOP_CODER, author, submissionDate);
        this.difficulty = difficulty;
        this.division = division;
    }

    @Column(name = "difficulty", nullable = false)
    public TopCoderDifficulty getDifficulty() {
        return difficulty;
    }

    @Column(name = "division", nullable = false)
    public TopCoderDivision getDivision() {
        return division;
    }

    public void setDifficulty(TopCoderDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDivision(TopCoderDivision division) {
        this.division = division;
    }
}
