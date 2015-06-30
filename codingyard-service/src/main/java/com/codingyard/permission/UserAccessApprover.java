package com.codingyard.permission;

import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Utility class that provides a set of method that returns true/false
 * if a user can do a CRUD operation on another user.
 */
public class UserAccessApprover {

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

    /**
     * Returns true if {@code user} can edit {@code targetUser}'s information.
     * {@code user} should be able to edit field(s) of {@code targetUser}'s info if the {@code user}
     * has strictly higher role than {@code targetUser} or if the {@code user} is a global admin.
     *
     * @param user       The user who needs to be authorized to edit {@code targetUser}
     * @param targetUser The user whom the user wants to edit.
     * @return true if {@code user} can edit {@code targetUser}'s information.
     */
    public static boolean canEdit(final CodingyardUser user, final CodingyardUser targetUser) {
        return hasHighPermission(user, targetUser) || user.getRole().equals(Role.GLOBAL_ADMIN);
    }

    /**
     * Returns true if {@code approver} can approve {@code targetUser}'s role change to {@code newRole}.
     * {@code approver} can approve {@code targetUser}'s role change only if the below conditions are met<br/>
     * 1) {@code approver} has strictly higher role level that {@code targetUser} or {@code approver} is a global admin.<br/>
     * 2) {@code approver} has higher or equal role level compared to {@code newRole}<br/>
     *
     * @param approver   The user who is trying to approve.
     * @param targetUser The user whose role is being changed.
     * @param newRole    The new role to which the targetUser will be changed.
     * @return true if {@code approver} can approve {@code targetUser}'s role change to {@code newRole}.
     */
    public static boolean canEditRole(final CodingyardUser approver, final CodingyardUser targetUser, final Role newRole) {
        return (hasHighPermission(approver, targetUser) || approver.getRole().equals(Role.GLOBAL_ADMIN)) && approver.getRole().getLevel() >= newRole.getLevel();
    }

    /**
     * Returns a list of roles that {@code approver} can approve for {@code targetUser}.
     * An empty list will be returned if there is no such role.
     */
    public static List<Role> getEditableRoles(final CodingyardUser approver, final CodingyardUser targetUser) {
        List<Role> editableRoles = Lists.newArrayList();
        for (final Role role : Role.values()) {
            if (canEditRole(approver, targetUser, role)) {
                editableRoles.add(role);
            }
        }
        return editableRoles;
    }

    /**
     * Returns true if {@code user} can edit {@code targetUser}'s password.
     */
    public static boolean canEditPassword(final CodingyardUser user, final CodingyardUser targetUser) {
        return user.getRole().equals(Role.GLOBAL_ADMIN) || user.equals(targetUser);
    }

    /**
     * Returns true if {@code user} can edit {@code targetUser}'s firstname.
     */
    public static boolean canEditFirstName(final CodingyardUser user, final CodingyardUser targetUser) {
        return user.getRole().equals(Role.GLOBAL_ADMIN) || user.equals(targetUser);
    }

    /**
     * Returns true if {@code user} can edit {@code targetUser}'s lastname.
     */
    public static boolean canEditLastName(final CodingyardUser user, final CodingyardUser targetUser) {
        return user.getRole().equals(Role.GLOBAL_ADMIN) || user.equals(targetUser);
    }

    /**
     * Returns true if {@code user} can edit {@code targetUser}'s email.
     */
    public static boolean canEditEmail(final CodingyardUser user, final CodingyardUser targetUser) {
        return user.getRole().equals(Role.GLOBAL_ADMIN) || user.equals(targetUser);
    }

    /**
     * Returns true if {@code user} can delete {@code targetUser}.
     * <p/>
     * User X can delete User Y when any of the following conditions are met.
     * 1) User X is a global admin.
     * 2) User X is an admin, and User Y is either a member or a guest.
     *
     * @param user       user who is deleting {@code targetUser}
     * @param targetUser user to be deleted
     * @return {@code true} if {@code user} can delete {@code targetUser}. {@code false} otherwise.
     */
    public static boolean canDelete(final CodingyardUser user, final CodingyardUser targetUser) {
        final Role userRole = user.getRole();
        final Role targetUserRole = targetUser.getRole();
        return userRole.equals(Role.GLOBAL_ADMIN)
            || (userRole.equals(Role.ADMIN) && (targetUserRole.equals(Role.MEMBER) || targetUserRole.equals(Role.GUEST)));
    }
}
