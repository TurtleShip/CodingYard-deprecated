package com.codingyard.permission;

import com.codingyard.api.entity.contest.Solution;
import com.codingyard.api.entity.user.CodingyardUser;
import org.junit.Before;
import org.junit.Test;

import static com.codingyard.api.entity.user.Role.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SolutionAccessApproverTest {

    /**
     * Technically, guest will never have a solution.
     * But testing CRUD on guest solutions for thoroughness of tests.
     */

    private final CodingyardUser globalAdmin = mock(CodingyardUser.class);
    private final CodingyardUser admin = mock(CodingyardUser.class);
    private final CodingyardUser member = mock(CodingyardUser.class);
    private final CodingyardUser guest = mock(CodingyardUser.class);

    private final CodingyardUser globalAdminTwo = mock(CodingyardUser.class);
    private final CodingyardUser adminTwo = mock(CodingyardUser.class);
    private final CodingyardUser memberTwo = mock(CodingyardUser.class);
    private final CodingyardUser guestTwo = mock(CodingyardUser.class);

    private final Solution globalAdminSolution = mock(Solution.class);
    private final Solution adminSolution = mock(Solution.class);
    private final Solution memberSolution = mock(Solution.class);
    private final Solution guestSolution = mock(Solution.class);

    private final Solution globalAdminSolutionTwo = mock(Solution.class);
    private final Solution adminSolutionTwo = mock(Solution.class);
    private final Solution memberSolutionTwo = mock(Solution.class);
    private final Solution guestSolutionTwo = mock(Solution.class);

    @Before
    public void setup() {
        when(globalAdmin.getRole()).thenReturn(GLOBAL_ADMIN);
        when(admin.getRole()).thenReturn(ADMIN);
        when(member.getRole()).thenReturn(MEMBER);
        when(guest.getRole()).thenReturn(GUEST);

        when(globalAdminTwo.getRole()).thenReturn(GLOBAL_ADMIN);
        when(adminTwo.getRole()).thenReturn(ADMIN);
        when(memberTwo.getRole()).thenReturn(MEMBER);
        when(guestTwo.getRole()).thenReturn(GUEST);

        when(globalAdminSolution.getAuthor()).thenReturn(globalAdmin);
        when(adminSolution.getAuthor()).thenReturn(admin);
        when(memberSolution.getAuthor()).thenReturn(member);
        when(guestSolution.getAuthor()).thenReturn(guest);

        when(globalAdminSolutionTwo.getAuthor()).thenReturn(globalAdminTwo);
        when(adminSolutionTwo.getAuthor()).thenReturn(adminTwo);
        when(memberSolutionTwo.getAuthor()).thenReturn(memberTwo);
        when(guestSolutionTwo.getAuthor()).thenReturn(guestTwo);
    }

    @Test
    public void globalAdminCanDeleteAllSolution() {
        assertTrue(SolutionAccessApprover.canDelete(globalAdmin, globalAdminSolution));
        assertTrue(SolutionAccessApprover.canDelete(globalAdmin, globalAdminSolutionTwo));
        assertTrue(SolutionAccessApprover.canDelete(globalAdmin, adminSolution));
        assertTrue(SolutionAccessApprover.canDelete(globalAdmin, memberSolution));
        assertTrue(SolutionAccessApprover.canDelete(globalAdmin, guestSolution));
    }

    @Test
    public void adminCanOnlyDeleteMemberSolution() {
        assertFalse(SolutionAccessApprover.canDelete(admin, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canDelete(admin, adminSolutionTwo));
        assertTrue(SolutionAccessApprover.canDelete(admin, adminSolution));
        assertTrue(SolutionAccessApprover.canDelete(admin, memberSolution));
        assertTrue(SolutionAccessApprover.canDelete(admin, guestSolution));
    }

    @Test
    public void memberCanOnlyDeleteItsOwnSolution() {
        assertFalse(SolutionAccessApprover.canDelete(member, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canDelete(member, adminSolution));
        assertFalse(SolutionAccessApprover.canDelete(member, memberSolutionTwo));
        assertTrue(SolutionAccessApprover.canDelete(member, memberSolution));
        assertFalse(SolutionAccessApprover.canDelete(member, guestSolution));
    }

    @Test
    public void guestCanOnlyDeleteItsOwnSolution() {
        assertFalse(SolutionAccessApprover.canDelete(guest, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canDelete(guest, adminSolution));
        assertFalse(SolutionAccessApprover.canDelete(guest, memberSolution));
        assertFalse(SolutionAccessApprover.canDelete(guest, guestSolutionTwo));
        assertTrue(SolutionAccessApprover.canDelete(guest, guestSolution));
    }

    @Test
    public void everyoneCanViewEachOthersSolution() {
        assertTrue(SolutionAccessApprover.canView(globalAdmin, globalAdminSolution));
        assertTrue(SolutionAccessApprover.canView(globalAdmin, globalAdminSolutionTwo));
        assertTrue(SolutionAccessApprover.canView(globalAdmin, adminSolution));
        assertTrue(SolutionAccessApprover.canView(globalAdmin, memberSolution));
        assertTrue(SolutionAccessApprover.canView(globalAdmin, guestSolution));

        assertTrue(SolutionAccessApprover.canView(admin, globalAdminSolution));
        assertTrue(SolutionAccessApprover.canView(admin, adminSolution));
        assertTrue(SolutionAccessApprover.canView(admin, adminSolutionTwo));
        assertTrue(SolutionAccessApprover.canView(admin, memberSolution));
        assertTrue(SolutionAccessApprover.canView(admin, guestSolution));

        assertTrue(SolutionAccessApprover.canView(member, globalAdminSolution));
        assertTrue(SolutionAccessApprover.canView(member, adminSolution));
        assertTrue(SolutionAccessApprover.canView(member, memberSolution));
        assertTrue(SolutionAccessApprover.canView(member, memberSolutionTwo));
        assertTrue(SolutionAccessApprover.canView(member, guestSolution));

        assertTrue(SolutionAccessApprover.canView(guest, globalAdminSolution));
        assertTrue(SolutionAccessApprover.canView(guest, globalAdminSolutionTwo));
        assertTrue(SolutionAccessApprover.canView(guest, adminSolution));
        assertTrue(SolutionAccessApprover.canView(guest, memberSolution));
        assertTrue(SolutionAccessApprover.canView(guest, guestSolution));
        assertTrue(SolutionAccessApprover.canView(guest, guestSolutionTwo));
    }

    @Test
    public void solutionCanBeEditedOnlyByItsAuthor() {
        assertFalse(SolutionAccessApprover.canEdit(globalAdmin, globalAdminSolutionTwo));
        assertTrue(SolutionAccessApprover.canEdit(globalAdmin, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canEdit(globalAdmin, adminSolution));
        assertFalse(SolutionAccessApprover.canEdit(globalAdmin, memberSolution));
        assertFalse(SolutionAccessApprover.canEdit(globalAdmin, guestSolution));

        assertFalse(SolutionAccessApprover.canEdit(admin, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canEdit(admin, adminSolutionTwo));
        assertTrue(SolutionAccessApprover.canEdit(admin, adminSolution));
        assertFalse(SolutionAccessApprover.canEdit(admin, memberSolution));
        assertFalse(SolutionAccessApprover.canEdit(admin, guestSolution));

        assertFalse(SolutionAccessApprover.canEdit(member, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canEdit(member, adminSolution));
        assertFalse(SolutionAccessApprover.canEdit(member, memberSolutionTwo));
        assertTrue(SolutionAccessApprover.canEdit(member, memberSolution));
        assertFalse(SolutionAccessApprover.canEdit(member, guestSolution));

        assertFalse(SolutionAccessApprover.canEdit(guest, globalAdminSolution));
        assertFalse(SolutionAccessApprover.canEdit(guest, adminSolution));
        assertFalse(SolutionAccessApprover.canEdit(guest, memberSolution));
        assertFalse(SolutionAccessApprover.canEdit(guest, guestSolutionTwo));
        assertTrue(SolutionAccessApprover.canEdit(guest, guestSolution));
    }

    @Test
    public void onlyMemberAndAboveCanCreateASolution() {
        assertTrue(SolutionAccessApprover.canCreate(globalAdmin));
        assertTrue(SolutionAccessApprover.canCreate(admin));
        assertTrue(SolutionAccessApprover.canCreate(member));
        assertFalse(SolutionAccessApprover.canCreate(guest));
    }
}
