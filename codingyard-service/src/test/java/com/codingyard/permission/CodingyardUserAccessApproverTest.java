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
public class CodingyardUserAccessApproverTest {

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

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GUEST));

        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, admin, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, admin, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, admin, GUEST));

        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, GUEST));

        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testAdminApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(ADMIN);

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testMemberApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(MEMBER);

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, ADMIN));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, MEMBER));
        assertTrue(UserAccessApprover.canApprove(currentCodingyardUser, guest, GUEST));
    }

    @Test
    public void testGuestApproval() {
        when(currentCodingyardUser.getRole()).thenReturn(GUEST);

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, globalAdmin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, admin, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, member, GUEST));

        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, GLOBAL_ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, ADMIN));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, MEMBER));
        assertFalse(UserAccessApprover.canApprove(currentCodingyardUser, guest, GUEST));
    }
}
