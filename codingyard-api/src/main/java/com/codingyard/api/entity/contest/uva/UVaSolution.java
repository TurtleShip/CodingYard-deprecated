package com.codingyard.api.entity.contest.uva;

import com.codingyard.api.entity.contest.Contest;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a solution to UVA Online Judge : https://uva.onlinejudge.org/
 */
@Entity
@Table(name = "uva_solution")
public class UVaSolution extends Solution {

    private Long problemNumber;

    // package private. Needed for hibernate.
    UVaSolution() {
        super();
    }

    @JsonCreator
    public UVaSolution(@JsonProperty("author") CodingyardUser author,
                       @NotNull @JsonProperty("submitted") Date submissionDate,
                       @NotNull @JsonProperty("language") Language language,
                       @NotNull @JsonProperty("problem_number") Long problemNumber) {
        this(author, submissionDate, "", language, problemNumber);

    }

    public UVaSolution(final CodingyardUser author,
                       final Date submissionDate,
                       final String filePath,
                       final Language language,
                       final Long problemNumber) {
        super(Contest.UVA_ONLINE_JUDGE, author, submissionDate, filePath, language);
        this.problemNumber = problemNumber;
    }

    @JsonProperty("problem_number")
    @Column(name = "problem_number", nullable = false)
    public Long getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(Long problemNumber) {
        this.problemNumber = problemNumber;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(problemNumber);
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
        final UVaSolution other = (UVaSolution) obj;
        return Objects.equals(this.problemNumber, other.problemNumber);
    }
}
