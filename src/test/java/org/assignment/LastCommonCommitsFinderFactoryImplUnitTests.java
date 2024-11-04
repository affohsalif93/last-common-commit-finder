package org.assignment;


import org.assignment.exceptions.InvalidTokenException;
import org.assignment.exceptions.RepositoryPrivateNotFoundException;
import org.assignment.impl.LastCommonCommitsFinderFactoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assignment.TestUtils.assertInCauseChain;
import static org.junit.jupiter.api.Assertions.*;


class LastCommonCommitsFinderFactoryImplUnitTests {
    static String VALID_OWNER = "torvalds";
    static String VALID_REPO = "linux";
    static String INVALID_REPO = "git";
    static String INVALID_TOKEN = "invalid_token";

    LastCommonCommitsFinderFactory finderFactory = new LastCommonCommitsFinderFactoryImpl();

    @Nested
    @DisplayName("Argument validation")
    class ArgumentValidation {
        @Test
        public void testInvalidOwnerName() {
            assertInCauseChain(IllegalArgumentException.class, () ->
                    finderFactory.create("", VALID_REPO, null)
            );
            assertInCauseChain(IllegalArgumentException.class, () ->
                    finderFactory.create(null, VALID_REPO, null)
            );
        }

        @Test
        public void testInvalidRepoName() {
            assertInCauseChain(IllegalArgumentException.class, () ->
                    finderFactory.create(VALID_OWNER, "", null)
            );
            assertInCauseChain(IllegalArgumentException.class, () ->
                    finderFactory.create(VALID_OWNER, null, null)
            );
        }
    }


    @Nested
    @DisplayName("Repository validation")
    class RepositoryValidation {
        @Test
        public void testPublicRepo() {
            assertDoesNotThrow(() ->
                    finderFactory.create(VALID_OWNER, VALID_REPO, null)
            );
        }

        @Test
        public void testGithubRepoNotFound() {
            assertInCauseChain(RepositoryPrivateNotFoundException.class, () ->
                    finderFactory.create(VALID_OWNER, INVALID_REPO, null)
            );
        }
    }

    @Nested
    @DisplayName("Token Validation")
    class TokenValidation {
        @Test
        public void testPublicRepoWithInvalidToken() {
            assertInCauseChain(InvalidTokenException.class, () ->
                    finderFactory.create(VALID_OWNER, VALID_REPO, INVALID_TOKEN)
            );
        }
    }
}
