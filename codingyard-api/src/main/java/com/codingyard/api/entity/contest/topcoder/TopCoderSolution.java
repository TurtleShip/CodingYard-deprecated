package com.codingyard.api.entity.contest.topcoder;

import com.codingyard.api.entity.contest.Contest;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "topcoder_solution")
public class TopCoderSolution extends Solution {

    private TopCoderDifficulty difficulty;
    private TopCoderDivision division;
    private Long problemNumber;

    @JsonCreator
    public TopCoderSolution(@Valid @JsonProperty("author") CodingyardUser author,
                            @NotNull @JsonProperty("submitted") Date submissionDate,
                            @NotNull @JsonProperty("language") Language language,
                            @NotNull @JsonProperty("difficulty") TopCoderDifficulty difficulty,
                            @NotNull @JsonProperty("division") TopCoderDivision division,
                            @NotNull @JsonProperty("problem_number") Long problemNumber) {
        this(author, submissionDate, "", language, difficulty, division, problemNumber);
    }


    public TopCoderSolution(CodingyardUser author,
                            Date submissionDate,
                            String filePath,
                            Language language,
                            TopCoderDifficulty difficulty,
                            TopCoderDivision division,
                            Long problemNumber) {
        super(Contest.TOP_CODER, author, submissionDate, filePath, language);
        this.difficulty = difficulty;
        this.division = division;
        this.problemNumber = problemNumber;
    }


    @JsonProperty("difficulty")
    @Column(name = "difficulty", nullable = false)
    public TopCoderDifficulty getDifficulty() {
        return difficulty;
    }

    @JsonProperty("division")
    @Column(name = "division", nullable = false)
    public TopCoderDivision getDivision() {
        return division;
    }

    @JsonProperty("problem_number")
    @Column(name = "problem_number", nullable = false)
    public Long getProblemNumber() {
        return problemNumber;
    }

    public void setDifficulty(TopCoderDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDivision(TopCoderDivision division) {
        this.division = division;
    }

    public void setProblemNumber(Long problemNumber) {
        this.problemNumber = problemNumber;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(difficulty, division, problemNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final TopCoderSolution other = (TopCoderSolution) obj;
        return Objects.equals(this.difficulty, other.difficulty)
            && Objects.equals(this.division, other.division)
            && Objects.equals(this.problemNumber, other.problemNumber);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("difficulty", difficulty)
            .add("division", division)
            .add("problemNumber", problemNumber)
            .toString();
    }
}
