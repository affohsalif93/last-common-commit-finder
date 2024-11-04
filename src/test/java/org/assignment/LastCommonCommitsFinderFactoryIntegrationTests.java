package org.assignment;


import org.assignment.exceptions.InvalidTokenException;
import org.assignment.exceptions.RepositoryPrivateNotFoundException;
import org.assignment.impl.LastCommonCommitsFinderFactoryImpl;
import org.assignment.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.assignment.TestUtils.assertInCauseChain;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LastCommonCommitsFinderFactoryIntegrationTests {
    private static String PRIVATE_VALID_TOKEN;
    private static String PRIVATE_OWNER;
    private static String PRIVATE_REPO;

    LastCommonCommitsFinderFactory finderFactory = new LastCommonCommitsFinderFactoryImpl();

    @BeforeAll
    public static void setup() {
        PRIVATE_OWNER = System.getenv("GITHUB_REPO_OWNER");
        PRIVATE_REPO = System.getenv("GITHUB_REPO_NAME");
        PRIVATE_VALID_TOKEN = System.getenv("GITHUB_REPO_TOKEN");

        System.out.println("PRIVATE_OWNER: " + PRIVATE_OWNER);
        System.out.println("PRIVATE_REPO: " + PRIVATE_REPO);
        System.out.println("PRIVATE_VALID_TOKEN: " + PRIVATE_VALID_TOKEN);
    }

    boolean privateRepoCredentialsAreSet() {
        return StringUtils.hasText(PRIVATE_VALID_TOKEN) &&
                StringUtils.hasText(PRIVATE_OWNER) &&
                StringUtils.hasText(PRIVATE_REPO);
    }

    @Test
    @EnabledIf("privateRepoCredentialsAreSet")
    public void testPrivateRepoWithValidToken() {
        assertDoesNotThrow(() ->
                finderFactory.create(PRIVATE_OWNER, PRIVATE_REPO, PRIVATE_VALID_TOKEN)
        );
    }

    @Test
    @EnabledIf("privateRepoCredentialsAreSet")
    public void testPrivateRepoWithInvalidToken() {
        assertInCauseChain(InvalidTokenException.class, () ->
                finderFactory.create(PRIVATE_OWNER, PRIVATE_REPO, "invalid_token")
        );
    }

    @Test
    @EnabledIf("privateRepoCredentialsAreSet")
    public void testPrivateRepoWithInvalidRepoName() {
        assertInCauseChain(RepositoryPrivateNotFoundException.class, () ->
                finderFactory.create(PRIVATE_OWNER, "invalid_repo", PRIVATE_VALID_TOKEN)
        );
    }
}
