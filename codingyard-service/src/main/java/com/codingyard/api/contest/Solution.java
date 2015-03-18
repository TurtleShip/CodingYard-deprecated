package com.codingyard.api.contest;

import com.codingyard.api.user.User;

import java.util.Date;

public class Solution {

    private final Contest contest;
    private final User author;
    private final Date submissionDate;

    private User authorizer;
    private Date acceptedDate;
    private boolean isAccepted;

    public Solution(final Contest contest, final User author, final Date submissionDate) {
        this.contest = contest;
        this.author = author;
        this.submissionDate = submissionDate;
        this.isAccepted = false;
    }

    public Contest getContest() {
        return contest;
    }

    public User getAuthor() {
        return author;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public User getAuthorizer() {
        return authorizer;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAuthorizer(User authorizer) {
        this.authorizer = authorizer;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}
