package org.assignment;


import org.assignment.exceptions.InvalidBranchException;
import org.assignment.impl.LastCommonCommitsFinderImpl;
import org.assignment.mocks.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assignment.TestUtils.assertInCauseChain;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LastCommonCommitsFinderImplIntegrationTests {
    LastCommonCommitsFinder validFinder = new LastCommonCommitsFinderImpl(new MultipleLastCommitsRepositoryServiceMock());

    @Test
    public void testSameBranch() {
        assertDoesNotThrow(() -> {
            var commitHistory = validFinder.findLastCommonCommits("main", "main");
            assertEquals(List.of("1", "2", "3", "5"), commitHistory);
        });
    }

    @Test
    public void testInvalidBranch() {
        assertInCauseChain(InvalidBranchException.class, () ->
                validFinder.findLastCommonCommits("main", "feature/awesome")
        );
    }

    @Test
    public void testMultipleLastCommonCommits() {
        assertDoesNotThrow(() -> {
            var commits = validFinder.findLastCommonCommits("main", "test");
            assertEquals(List.of("1", "2"), commits);
        });
    }
}
