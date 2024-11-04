package org.assignment.impl;

import org.assignment.*;
import org.assignment.exceptions.*;
import org.assignment.utils.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class LastCommonCommitsFinderFactoryImpl implements LastCommonCommitsFinderFactory {

    private static final String GITHUB_API_BASE_URL = "https://api.github.com/repos";
    private final HttpClient httpClient;
    private final HttpRequest.Builder requestBuilder;

    public LastCommonCommitsFinderFactoryImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET();
    }

    public LastCommonCommitsFinderFactoryImpl(HttpClient httpClient, HttpRequest.Builder baseRequestBuilder, GithubHttpCacheConfig cacheConfig) {
        this.httpClient = httpClient;
        this.requestBuilder = baseRequestBuilder;
    }


    /**
     * check if the Github repository is valid given the URL and the access token
     *
     * @param url URL of the repository
     * @throws InvalidTokenException          if the token is invalid
     * @throws IOException                    if an I/O error occurs or if the operation is interrupted
     * @throws HttpRequestException           if the request fails
     * @throws RateLimitOrPermissionException if the rate limit is exceeded or the user does not have permission
     */
    private void validateGithubRepository(String url) throws InvalidTokenException, IOException, HttpRequestException, RateLimitOrPermissionException {
        try {
            var response = httpClient.send(
                requestBuilder.uri(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofString()
            );
            GithubHttpResponse.validateHttpResponse(GithubHttpResponse.of(response));
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    /**
     * Create a LastCommonCommitsFinder object
     *
     * @param owner owner of the repository
     * @param repo  name of the repository
     * @param token access token
     * @return LastCommonCommitsFinder object
     * @throws IOException if an I/O error occurs or if the operation is interrupted
     */
    @Override
    public LastCommonCommitsFinder create(String owner, String repo, String token) throws IOException {
        owner = StringUtils.stripIfExist(owner);
        repo = StringUtils.stripIfExist(repo);
        token = StringUtils.stripIfExist(token);

        try {
            if (!StringUtils.hasText(owner)) {
                throw new IllegalArgumentException("Owner cannot be empty");
            }
            if (!StringUtils.hasText(repo)) {
                throw new IllegalArgumentException("Repository name cannot be empty");
            }

            if (StringUtils.hasText(token)) {
                requestBuilder.header("Authorization", "Bearer " + token.strip());
            }

            String repositoryURL = GITHUB_API_BASE_URL + "/" + owner + "/" + repo;
            validateGithubRepository(repositoryURL);

            var repositoryService = new GithubRepositoryServiceImpl(
                httpClient,
                requestBuilder,
                repositoryURL,
                GithubHttpCacheConfig.of(100, Duration.ofMinutes(10))
            );

            return new LastCommonCommitsFinderImpl(repositoryService);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
