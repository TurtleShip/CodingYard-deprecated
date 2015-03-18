package com.codingyard.util;


import com.codingyard.api.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.codingyard.api.user.Role.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRoleApproverTest {

    @Mock
    private User globalAdmin;

    @Mock
    private User admin;

    @Mock
    private User member;

    @Mock
    private User guest;

    @Mock
    private User currentUser;

    @Before
    public void setUp() {
        when(globalAdmin.getRole()).thenReturn(GLOBAL_ADMIN);
        when(admin.getRole()).thenReturn(ADMIN);
        when(member.getRole()).thenReturn(MEMBER);
        when(guest.getRole()).thenReturn(GUEST);
    }

    @Test
    public void testGlobalAdminApproval() {
        when(currentUser.getRole()).thenReturn(GLOBAL_ADMIN);

        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GUEST));

        assertTrue(UserRoleApprover.approve(currentUser, admin, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, admin, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, admin, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, admin, GUEST));

        assertTrue(UserRoleApprover.approve(currentUser, member, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, member, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, member, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, member, GUEST));

        assertTrue(UserRoleApprover.approve(currentUser, guest, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, guest, GUEST));
    }

    @Test
    public void testAdminApproval() {
        when(currentUser.getRole()).thenReturn(ADMIN);

        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, member, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, member, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, member, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, guest, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, guest, GUEST));
    }

    @Test
    public void testMemberApproval() {
        when(currentUser.getRole()).thenReturn(MEMBER);

        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, member, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, member, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, member, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, guest, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentUser, guest, GUEST));
    }

    @Test
    public void testGuestApproval() {
        when(currentUser.getRole()).thenReturn(GUEST);

        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, member, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, member, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, member, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentUser, guest, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, guest, ADMIN));
        assertFalse(UserRoleApprover.approve(currentUser, guest, MEMBER));
        assertFalse(UserRoleApprover.approve(currentUser, guest, GUEST));
    }
}
