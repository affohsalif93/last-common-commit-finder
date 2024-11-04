package org.assignment.mocks;

import org.assignment.GithubHttpResponse;
import org.assignment.AbstractRepositoryService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EmptyBodyRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, "");
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, "");
    }
}
