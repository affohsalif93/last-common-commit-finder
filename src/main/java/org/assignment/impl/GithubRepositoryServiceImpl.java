package org.assignment.impl;

import org.assignment.AbstractRepositoryService;
import org.assignment.GithubHttpCacheConfig;
import org.assignment.GithubHttpResponse;
import org.assignment.GithubHttpOkResponseCache;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GithubRepositoryServiceImpl extends AbstractRepositoryService {
    private final HttpClient httpClient;
    private final HttpRequest fetchBranchesRequest;
    private final HttpRequest fetchCommitsRequest;
    private final GithubHttpOkResponseCache cache;


    public GithubRepositoryServiceImpl(HttpClient httpClient, HttpRequest.Builder baseRequestBuilder, String repositoryURL, GithubHttpCacheConfig cacheConfig) {
        this.httpClient = httpClient;
        this.fetchBranchesRequest = baseRequestBuilder.uri(URI.create(repositoryURL + "/branches")).build();
        this.fetchCommitsRequest = baseRequestBuilder.uri(URI.create(repositoryURL + "/commits")).build();
        this.cache = new GithubHttpOkResponseCache(cacheConfig.capacity(), cacheConfig.timeToLive());
    }

    @Override
    protected GithubHttpResponse fetchBranches() throws IOException, InterruptedException {
        String url = fetchBranchesRequest.uri().toString();
        String cachedResponse = cache.get(url);
        if (cachedResponse != null) {
            return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, cachedResponse);
        }
        var response = GithubHttpResponse.of(httpClient.send(fetchBranchesRequest, HttpResponse.BodyHandlers.ofString()));
        cache.put(url, response);
        return response;
    }


    @Override
    protected GithubHttpResponse fetchCommits() throws IOException, InterruptedException {
        String url = fetchCommitsRequest.uri().toString();
        String cachedResponse = cache.get(url);
        if (cachedResponse != null) {
            return GithubHttpResponse.of(HttpURLConnection.HTTP_OK, cachedResponse);
        }
        var response = GithubHttpResponse.of(httpClient.send(fetchCommitsRequest, HttpResponse.BodyHandlers.ofString()));
        cache.put(url, response);
        return response;
    }
}
