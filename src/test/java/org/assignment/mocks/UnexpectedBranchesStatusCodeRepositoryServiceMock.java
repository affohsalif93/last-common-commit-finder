package org.assignment.mocks;

import org.assignment.AbstractRepositoryService;
import org.assignment.GithubHttpResponse;

import java.net.HttpURLConnection;

public class UnexpectedBranchesStatusCodeRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_INTERNAL_ERROR, "ignored body");
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, "");
    }
}
