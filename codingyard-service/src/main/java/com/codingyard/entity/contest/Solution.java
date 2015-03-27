package com.codingyard.entity.contest;

import com.codingyard.entity.user.CodingyardUser;

import java.nio.file.Path;
import java.util.Date;

public class Solution {

    private final Contest contest;
    private final CodingyardUser author;
    private final Date submissionDate;
    private Path filePath;

    private CodingyardUser authorizer; // The user who accepted this solution
    private Date acceptedDate; // The date when this solution has been accepted
    private boolean isAccepted; // True is this solution has been accepted

    /**
     *
     * @param contest The contest this solution belongs to
     * @param author The author of this solution
     * @param submissionDate The date when the solution was submitted
     * @param filePath The path where this solution has been saved
     */
    public Solution(final Contest contest, final CodingyardUser author, final Date submissionDate, final Path filePath) {
        this.contest = contest;
        this.author = author;
        this.submissionDate = submissionDate;
        this.filePath = filePath;
        this.isAccepted = false;
    }

    public Contest getContest() {
        return contest;
    }

    public CodingyardUser getAuthor() {
        return author;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Path getFilePath() {
        return filePath;
    }

    public CodingyardUser getAuthorizer() {
        return authorizer;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAuthorizer(CodingyardUser authorizer) {
        this.authorizer = authorizer;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
}