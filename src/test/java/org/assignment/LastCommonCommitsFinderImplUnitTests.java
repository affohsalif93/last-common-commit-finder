package org.assignment;


import org.assignment.exceptions.EmptyResponseException;
import org.assignment.exceptions.HttpRequestException;
import org.assignment.exceptions.InvalidBranchException;
import org.assignment.exceptions.ParsingException;
import org.assignment.impl.LastCommonCommitsFinderImpl;
import org.assignment.mocks.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assignment.TestUtils.assertInCauseChain;
import static org.junit.jupiter.api.Assertions.*;

public class LastCommonCommitsFinderImplUnitTests {
    /*
     * 1 -> 2 -> 3 -> 5
     *      |-> 4
     */
    @Nested
    @DisplayName("Invalid Arguments")
    class InvalidArguments {
        LastCommonCommitsFinder validFinder = new LastCommonCommitsFinderImpl(new MultipleLastCommitsRepositoryServiceMock());

        @Test
        public void testEmptyBranchNames() {
            assertInCauseChain(IllegalArgumentException.class, () ->
                validFinder.findLastCommonCommits("", "")
            );
        }

        @Test
        public void testNullBranchNames() {
            assertInCauseChain(IllegalArgumentException.class, () ->
                validFinder.findLastCommonCommits(null, null)
            );
        }
    }

    @Nested
    @DisplayName("Valid Repository")
    class ValidRepository {
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

        @Test
        public void testSingleLastCommonCommitWithSameHistoryLine() {
            var sameHistoryLineFinder = new LastCommonCommitsFinderImpl(new SameHistoryLineRepositoryServiceMock());
            assertDoesNotThrow(() -> {
                var commits = sameHistoryLineFinder.findLastCommonCommits("main", "test");
                assertEquals(List.of("3"), commits);
            });
        }

        @Test
        public void testSingleLastCommonCommit() {
            /*
             *    2     4
             *   /  \ /
             * 1 --- 3 --- 5
             */
            LastCommonCommitsFinder singleFinder = new LastCommonCommitsFinderImpl(new SingleLastCommitRepositoryServiceMock());
            assertDoesNotThrow(() -> {
                var commits = singleFinder.findLastCommonCommits("main", "test");
                assertEquals(List.of("3"), commits);
            });
        }
    }

    @Nested
    @DisplayName("Empty Body")
    class EmptyBody {
        LastCommonCommitsFinder emptyFinder = new LastCommonCommitsFinderImpl(new EmptyBodyRepositoryServiceMock());

        @Test
        public void testEmptyBranches() {
            assertInCauseChain(EmptyResponseException.class, () ->
                emptyFinder.findLastCommonCommits("main", "test")
            );
        }

        @Test
        public void testEmptyCommits() {
            assertInCauseChain(EmptyResponseException.class, () ->
                    emptyFinder.findLastCommonCommits("main", "test")
            );
        }
    }

    @Nested
    @DisplayName("Invalid Format")
    class InvalidFormat {
        LastCommonCommitsFinder invalidFinder = new LastCommonCommitsFinderImpl(new InvalidFormatRepositoryServiceMock());

        @Test
        public void testInvalidBranchesResult() {
            assertInCauseChain(ParsingException.class, () ->
                invalidFinder.findLastCommonCommits("main", "test")
            );
        }

        @Test
        public void testInvalidCommitsResult() {
            assertInCauseChain(ParsingException.class, () ->
                invalidFinder.findLastCommonCommits("main", "test")
            );
        }
    }

    @Nested
    @DisplayName("Unexpected Http Status")
    class UnexpectedStatusCode {
        @Test
        public void testUnexpectedBranchesStatusCode() {
            LastCommonCommitsFinder finder = new LastCommonCommitsFinderImpl(new UnexpectedBranchesStatusCodeRepositoryServiceMock());
            assertInCauseChain(HttpRequestException.class, () ->
                finder.findLastCommonCommits("main", "test")
            );
        }

        @Test
        public void testUnexpectedCommitsStatusCode() {
            LastCommonCommitsFinder finder = new LastCommonCommitsFinderImpl(new UnexpectedCommitsStatusCodeRepositoryServiceMock());
            assertInCauseChain(HttpRequestException.class, () ->
                    finder.findLastCommonCommits("main", "test")
            );
        }
    }

}
