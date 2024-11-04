package org.assignment.mocks;

import org.assignment.GithubHttpResponse;
import org.assignment.AbstractRepositoryService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class InvalidFormatRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        // invalid name field
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
           [
              {"nme":"main","commit":{"sha":"5"}},
              {"name":"test","commit":{"sha":"4"}}
           ]
        """);
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
            [
             {"sha":"5","parents":[{"sha":"3"}]},
             {"sha":"4","parents":[{"sha":"2"}]},
             {"sha":"3","parents":[{"sha":"2"}]},
             {"sha":"2","parents":[{"sha":"1"}]},
             {"sha":"1","parents":[]}
            ]
        """);
    }
}
