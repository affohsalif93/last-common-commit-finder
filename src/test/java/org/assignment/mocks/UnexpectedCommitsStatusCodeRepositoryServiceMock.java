package org.assignment.mocks;

import org.assignment.AbstractRepositoryService;
import org.assignment.GithubHttpResponse;

import java.net.HttpURLConnection;

public class UnexpectedCommitsStatusCodeRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
                    [
                       {"name":"main","commit":{"sha":"5"}},
                       {"name":"test","commit":{"sha":"4"}}
                    ]
                """);
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_INTERNAL_ERROR, "ignored body");
    }
}
