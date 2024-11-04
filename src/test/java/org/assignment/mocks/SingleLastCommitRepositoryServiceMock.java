package org.assignment.mocks;

import org.assignment.AbstractRepositoryService;
import org.assignment.GithubHttpResponse;

import java.net.HttpURLConnection;

/*
*    2     4
*   /  \ /
* 1 --- 3 --- 5
*/
public class SingleLastCommitRepositoryServiceMock extends AbstractRepositoryService {
    @Override
    protected GithubHttpResponse fetchBranches() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
           [
              {"name":"main","commit":{"sha":"4"}},
              {"name":"test","commit":{"sha":"5"}}
           ]
        """);
    }

    @Override
    protected GithubHttpResponse fetchCommits() {
        return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, """
            [
             {"sha":"5","parents":[{"sha":"3"}]},
             {"sha":"4","parents":[{"sha":"3"}]},
             {"sha":"3","parents":[{"sha":"2"},{"sha":"1"}]},
             {"sha":"2","parents":[{"sha":"1"}]},
             {"sha":"1","parents":[]}
            ]
        """);
    }
}
