package com.codingyard.api.entity.contest;

import com.codingyard.api.entity.user.CodingyardUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Solution {

    private Long solutionId;
    private Contest contest;
    private CodingyardUser author;
    private Date submissionDate;
    private String filePath;
    private Language language;

    // Needed for Hibernate
    protected Solution() {
    }

    /**
     * @param contest        The contest this solution belongs to
     * @param author         The author of this solution
     * @param submissionDate The date when the solution was submitted
     * @param filePath       The path where this solution is saved locally
     * @param language       The language in which this solution is written
     */
    public Solution(final Contest contest,
                    final CodingyardUser author,
                    final Date submissionDate,
                    final String filePath,
                    final Language language) {
        this.contest = contest;
        this.author = author;
        this.submissionDate = submissionDate;
        this.filePath = filePath;
        this.language = language;
    }

    @JsonProperty("solution_id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "solution_id", unique = true, nullable = false)
    public Long getSolutionId() {
        return solutionId;
    }

    @JsonProperty("contest")
    @Column(name = "contest", nullable = false)
    public Contest getContest() {
        return contest;
    }

    @JsonProperty("author")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public CodingyardUser getAuthor() {
        return author;
    }

    @JsonProperty("submitted")
    @Column(name = "submitted", nullable = false)
    public Date getSubmissionDate() {
        return submissionDate;
    }

    @JsonIgnore
    @Column(name = "file_path", nullable = false)
    public String getFilePath() {
        return filePath;
    }

    @JsonProperty("language")
    @Column(name = "language", nullable = false)
    public Language getLanguage() {
        return language;
    }

    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public void setAuthor(CodingyardUser author) {
        this.author = author;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(solutionId, contest, author, submissionDate, language);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Solution other = (Solution) obj;
        return Objects.equals(this.solutionId, other.solutionId)
            && Objects.equals(this.contest, other.contest)
            && Objects.equals(this.author, other.author)
            && Objects.equals(this.submissionDate, other.submissionDate)
            && Objects.equals(this.language, other.language);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("solutionId", solutionId)
            .add("contest", contest)
            .add("author", author)
            .add("submissionDate", submissionDate)
            .add("filePath", filePath)
            .add("language", language)
            .toString();
    }
}
