package com.codingyard.permission;

import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;

/**
 * Utility class that provides a set of method that returns true/false
 * if a user can do a CRUD operation on a solution.
 */
public class SolutionAccessApprover {

    /**
     * {@code user} can delete {@code solution} if one of the below conditions satisfies
     * 1) the user is the author of the solution.
     * 2) the user is an admin and the author is not a global admin.
     * 3) the user ia a global admin.
     *
     * @param user     user who wants to delete {@code solution}
     * @param solution solution to be deleted.
     * @return {@code true} if {@code user} can delete {@code solution}. {@code false} otherwise.
     */
    public static boolean canDelete(final CodingyardUser user, final Solution solution) {
        return user.equals(solution.getAuthor())
            || (user.getRole().equals(Role.ADMIN) && UserRoleApprover.hasHighPermission(user, solution.getAuthor()))
            || user.getRole().equals(Role.GLOBAL_ADMIN);
    }

    /**
     * Everyone can view each other's solution.
     *
     * @param user     user who wants to view {@code solution}
     * @param solution solution to be viewed.
     * @return {@code true} always.
     */
    public static boolean canView(final CodingyardUser user, final Solution solution) {
        return true;
    }

    /**
     * Only the author can edit its solution.
     *
     * @param user     user who wants to edit {@code solution}
     * @param solution solution to be edited.
     * @return {@code true} if the user can edit the solution. {@code false} otherwise.
     */
    public static boolean canEdit(final CodingyardUser user, final Solution solution) {
        return user.equals(solution.getAuthor());
    }

    /**
     * Only a member or above can create a solution.
     *
     * @param user user who wants to create a solution.
     * @return {@code true} if the user can create a solution.
     */
    public static boolean canCreate(final CodingyardUser user) {
        return user.getRole().getLevel() >= Role.MEMBER.getLevel();
    }
}
