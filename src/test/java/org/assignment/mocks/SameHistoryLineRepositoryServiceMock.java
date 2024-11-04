package org.assignment.mocks;

import org.assignment.AbstractRepositoryService;
import org.assignment.GithubHttpResponse;

import java.net.HttpURLConnection;

/*
*       2     4
*      /  \ /
*    1 --- 3
*/
public class SameHistoryLineRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
           [
              {"name":"main","commit":{"sha":"3"}},
              {"name":"test","commit":{"sha":"4"}}
           ]
        """);
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
            [
             {"sha":"4","parents":[{"sha":"3"}]},
             {"sha":"3","parents":[{"sha":"2"}, {"sha":"1"}]},
             {"sha":"2","parents":[{"sha":"1"}]},
             {"sha":"1","parents":[]}
            ]
        """);
    }
}
