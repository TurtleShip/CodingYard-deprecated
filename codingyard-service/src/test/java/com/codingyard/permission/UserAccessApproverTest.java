package com.codingyard.permission;


import com.codingyard.api.entity.user.CodingyardUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.codingyard.api.entity.user.Role.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccessApproverTest {

    @Mock
    private CodingyardUser globalAdmin;

    @Mock
    private CodingyardUser admin;

    @Mock
    private CodingyardUser member;

    @Mock
    private CodingyardUser guest;

    @Mock
    private CodingyardUser currentCodingyardUser;

    @Before
    public void setUp() {
        when(globalAdmin.getRole()).thenReturn(GLOBAL_ADMIN);
        when(admin.getRole()).thenReturn(ADMIN);
        when(member.getRole()).thenReturn(MEMBER);
        when(guest.getRole()).thenReturn(GUEST);
    }

    @Test
    public void testGlobalAdminApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(GLOBAL_ADMIN);

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GUEST));

        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, admin, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, admin, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GUEST));

        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, GUEST));

        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testAdminApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(ADMIN);

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testMemberApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(MEMBER);

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testGuestApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(GUEST);

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, ADMIN));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, MEMBER));
        assertFalse(UserAccessApprover.canEditRole(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void globalAdminCanDeleteAnyone() {
        assertTrue(UserAccessApprover.canDelete(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canDelete(globalAdmin, admin));
        assertTrue(UserAccessApprover.canDelete(globalAdmin, member));
        assertTrue(UserAccessApprover.canDelete(globalAdmin, guest));
    }

    @Test
    public void adminCanDeleteMemberAndGuest() {
        assertFalse(UserAccessApprover.canDelete(admin, globalAdmin));
        assertFalse(UserAccessApprover.canDelete(admin, admin));
        assertTrue(UserAccessApprover.canDelete(admin, member));
        assertTrue(UserAccessApprover.canDelete(admin, guest));
    }

    @Test
    public void memberCannotDeleteAnyone() {
        assertFalse(UserAccessApprover.canDelete(member, globalAdmin));
        assertFalse(UserAccessApprover.canDelete(member, admin));
        assertFalse(UserAccessApprover.canDelete(member, member));
        assertFalse(UserAccessApprover.canDelete(member, guest));
    }

    @Test
    public void guestCannotDeleteAnyone() {
        assertFalse(UserAccessApprover.canDelete(guest, globalAdmin));
        assertFalse(UserAccessApprover.canDelete(guest, admin));
        assertFalse(UserAccessApprover.canDelete(guest, member));
        assertFalse(UserAccessApprover.canDelete(guest, guest));
    }

    @Test
    public void globalAdminCanEditAnyPassword() {
        assertTrue(UserAccessApprover.canEditPassword(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditPassword(globalAdmin, admin));
        assertTrue(UserAccessApprover.canEditPassword(globalAdmin, member));
        assertTrue(UserAccessApprover.canEditPassword(globalAdmin, guest));
    }

    @Test
    public void anyoneCanEditItsOwnPassword() {
        assertTrue(UserAccessApprover.canEditPassword(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditPassword(admin, admin));
        assertTrue(UserAccessApprover.canEditPassword(member, member));
        assertTrue(UserAccessApprover.canEditPassword(guest, guest));
    }

    @Test
    public void globalAdminCanEditAnyFirstName() {
        assertTrue(UserAccessApprover.canEditFirstName(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditFirstName(globalAdmin, admin));
        assertTrue(UserAccessApprover.canEditFirstName(globalAdmin, member));
        assertTrue(UserAccessApprover.canEditFirstName(globalAdmin, guest));
    }

    @Test
    public void anyoneCanEditItsOwnFirstName() {
        assertTrue(UserAccessApprover.canEditFirstName(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditFirstName(admin, admin));
        assertTrue(UserAccessApprover.canEditFirstName(member, member));
        assertTrue(UserAccessApprover.canEditFirstName(guest, guest));
    }

    @Test
    public void globalAdminCanEditAnyLastName() {
        assertTrue(UserAccessApprover.canEditLastName(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditLastName(globalAdmin, admin));
        assertTrue(UserAccessApprover.canEditLastName(globalAdmin, member));
        assertTrue(UserAccessApprover.canEditLastName(globalAdmin, guest));
    }

    @Test
    public void anyoneCanEditItsOwnLastName() {
        assertTrue(UserAccessApprover.canEditLastName(globalAdmin, globalAdmin));
        assertTrue(UserAccessApprover.canEditLastName(admin, admin));
        assertTrue(UserAccessApprover.canEditLastName(member, member));
        assertTrue(UserAccessApprover.canEditLastName(guest, guest));
    }
}
