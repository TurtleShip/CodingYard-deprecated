package com.codingyard.entity.contest.topcoder;

import com.codingyard.entity.contest.Contest;
import com.codingyard.entity.contest.Language;
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
    private Long problemId;

    public TopCoderSolution(CodingyardUser author,
                            Date submissionDate,
                            String filePath,
                            Language language,
                            TopCoderDifficulty difficulty,
                            TopCoderDivision division,
                            Long problemId) {
        super(Contest.TOP_CODER, author, submissionDate, filePath, language);
        this.difficulty = difficulty;
        this.division = division;
        this.problemId = problemId;
    }

    @Column(name = "difficulty", nullable = false)
    public TopCoderDifficulty getDifficulty() {
        return difficulty;
    }

    @Column(name = "division", nullable = false)
    public TopCoderDivision getDivision() {
        return division;
    }

    @Column(name = "problem_id", nullable = false)
    public Long getProblemId() {
        return problemId;
    }

    public void setDifficulty(TopCoderDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDivision(TopCoderDivision division) {
        this.division = division;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }
}
