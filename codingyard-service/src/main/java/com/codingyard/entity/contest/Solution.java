package com.codingyard.entity.contest;

import com.codingyard.entity.user.CodingyardUser;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solution")
public class Solution {

    private Long solutionId;
    private Contest contest;
    private CodingyardUser author;
    private Date submissionDate;
    private String filePath;

    // package private. Needed for Hibernate
    Solution() {
    }

    /**
     * @param contest        The contest this solution belongs to
     * @param author         The author of this solution
     * @param submissionDate The date when the solution was submitted
     */
    public Solution(final Contest contest, final CodingyardUser author, final Date submissionDate) {
        this.contest = contest;
        this.author = author;
        this.submissionDate = submissionDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "solution_id", unique = true, nullable = false)
    public Long getSolutionId() {
        return solutionId;
    }

    @Column(name = "contest", nullable = false)
    public Contest getContest() {
        return contest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public CodingyardUser getAuthor() {
        return author;
    }

    @Column(name = "submitted", nullable = false)
    public Date getSubmissionDate() {
        return submissionDate;
    }

    @Column(name = "file_path", nullable = false)
    public String getFilePath() {
        return filePath;
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
}
