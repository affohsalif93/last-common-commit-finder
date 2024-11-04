package org.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GithubHttpOkRequestCacheUnitTests {

    private static GithubHttpOkResponseCache cache;
    private static final String BRANCHES_URL = "https://api.github.com/repos/owner/repo/branches";
    private static final String COMMITS_URL = "https://api.github.com/repos/owner/repo/commits";
    private static final String BRANCHES_RESPONSE = "branches data";
    private static final String COMMITS_RESPONSE = "commits data";

    @BeforeEach
    public void setup() {
        cache = new GithubHttpOkResponseCache(100, Duration.ofMinutes(2));
    }

    @Test
    public void testCacheStoresAndRetrievesOkResponseData() {
        cache.put(BRANCHES_URL, GithubHttpResponse.of(HttpURLConnection.HTTP_OK, BRANCHES_RESPONSE));
        cache.put(COMMITS_URL, GithubHttpResponse.of(HttpURLConnection.HTTP_OK, COMMITS_RESPONSE));

        assertEquals(BRANCHES_RESPONSE, cache.get(BRANCHES_URL));
        assertEquals(COMMITS_RESPONSE, cache.get(COMMITS_URL));
    }

    @Test
    public void testCacheIgnoresNonOkResponseData() {
        cache.put(COMMITS_URL, GithubHttpResponse.of(HttpURLConnection.HTTP_UNAUTHORIZED, COMMITS_RESPONSE));
        assertNull(cache.get(COMMITS_URL));
    }

    @Test
    public void testCacheExpiresData() throws InterruptedException {
        cache = new GithubHttpOkResponseCache(10, Duration.ofMillis(100));

        cache.put(COMMITS_URL, GithubHttpResponse.of(HttpURLConnection.HTTP_OK, COMMITS_RESPONSE));
        assertEquals(COMMITS_RESPONSE, cache.get(COMMITS_URL));

        Thread.sleep(150);
        assertNull(cache.get(COMMITS_URL));
    }

    @Test
    public void testCacheEvictsOldEntriesWhenMaxSizeExceeded() {
        cache = new GithubHttpOkResponseCache(2, Duration.ofMinutes(3));

        cache.put("url1", GithubHttpResponse.of(HttpURLConnection.HTTP_OK, COMMITS_RESPONSE));
        cache.put("url2", GithubHttpResponse.of(HttpURLConnection.HTTP_OK, COMMITS_RESPONSE));
        cache.put("url3", GithubHttpResponse.of(HttpURLConnection.HTTP_OK, COMMITS_RESPONSE));

        assertNull(cache.get("url1"));
        assertEquals(COMMITS_RESPONSE, cache.get("url2"));
        assertEquals(COMMITS_RESPONSE, cache.get("url3"));
    }
}
