package org.assignment;

import java.time.Duration;

public class GithubHttpCacheConfig {
    private final int capacity;
    private final Duration timeToLive;

    private GithubHttpCacheConfig(int capacity, Duration timeToLive) {
        this.capacity = capacity;
        this.timeToLive = timeToLive;
    }

    public static GithubHttpCacheConfig of(int capacity, Duration timeToLive) {
        return new GithubHttpCacheConfig(capacity, timeToLive);
    }

    public int capacity() {
        return capacity;
    }

    public Duration timeToLive() {
        return timeToLive;
    }
}
