package com.codingyard.util;

import com.codingyard.api.entity.user.Role;
import com.codingyard.api.entity.user.CodingyardUser;

public class UserRoleApprover {


    /**
     * Returns true if {@code approver} successfully approved {@code targetUser}'s role change to {@code newRole}.
     * {@code approver} can approve {@code targetUser}'s role change only if the below conditions are met<br/>
     * 1) {@code approver} has strictly higher role level that {@code targetUser}<br/>
     * 2) {@code approver} has higher or equal role level compared to {@code newRole}<br/>
     *
     * @param approver   The user who is trying to approve.
     * @param targetCodingyardUser The user whose role is being changed.
     * @param newRole    The new role to which the targetUser will be changed.
     * @return true if {@code approver} successfully approved {@code targetUser}'s role change to {@code newRole}.
     */
    public static boolean approve(final CodingyardUser approver, final CodingyardUser targetCodingyardUser, final Role newRole) {
        if (hasHighPermission(approver, targetCodingyardUser) && approver.getRole().getLevel() >= newRole.getLevel()) {
            targetCodingyardUser.setRole(newRole);
            return true;
        }
        return false;
    }

    /**
     * Returns true if user {@code higher} has strictly higher permission than user {@code lower}.
     * Note that the method will return {@code false} if lower users have the same permission.
     *
     * @param higher The user whose permission should be higher.
     * @param lower  The user whose permission should be lower.
     * @return true if {@code higher} has higher permission that {@code lower}
     */
    public static boolean hasHighPermission(final CodingyardUser higher, final CodingyardUser lower) {
        return higher.getRole().getLevel() > lower.getRole().getLevel();
    }

}
