package org.assignment.mocks;

import org.assignment.GithubHttpResponse;
import org.assignment.AbstractRepositoryService;

import java.net.HttpURLConnection;

/*
 * 1 -> 2 -> 3 -> 5
 *      |-> 4
 */
public class MultipleLastCommitsRepositoryServiceMock extends AbstractRepositoryService {
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
