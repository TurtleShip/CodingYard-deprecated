package com.codingyard.permission;


import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.permission.UserRoleApprover;
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
public class CodingyardUserRoleApproverTest {

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

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GUEST));

        assertTrue(UserRoleApprover.approve(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, admin, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, admin, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, admin, GUEST));

        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, GUEST));

        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testAdminApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(ADMIN);

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testMemberApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(MEMBER);

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserRoleApprover.approve(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testGuestApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(GUEST);

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, admin, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, member, GUEST));

        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, ADMIN));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, MEMBER));
        assertFalse(UserRoleApprover.approve(currentCodingyardUser, guest, GUEST));
    }
}
