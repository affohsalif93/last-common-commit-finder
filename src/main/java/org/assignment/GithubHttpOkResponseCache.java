package org.assignment;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.net.HttpURLConnection;
import java.time.Duration;

public class GithubHttpOkResponseCache {
    private final Cache<String, String> cache;

    public GithubHttpOkResponseCache(int capacity, Duration expireAfterWriteDuration) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(capacity)
                .expireAfterWrite(expireAfterWriteDuration)
                .build();
    }

    public void put(String key, GithubHttpResponse response) {
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            cache.put(key, response.body());
        } else {
            cache.invalidate(key);
        }
    }

    public String get(String key) {
        return cache.getIfPresent(key);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public long size() {
        return cache.size();
    }
}