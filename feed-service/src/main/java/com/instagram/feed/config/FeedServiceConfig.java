package com.instagram.feed.config;

import org.springframework.stereotype.Component;

/**
 * Configuration for Feed Service.
 */
@Component
public class FeedServiceConfig {

    private long celebrityThreshold = 100_000;
    private int defaultPageSize = 20;
    private int maxFeedSize = 1000;

    public long getCelebrityThreshold() { return celebrityThreshold; }
    public void setCelebrityThreshold(long celebrityThreshold) { this.celebrityThreshold = celebrityThreshold; }
    public int getDefaultPageSize() { return defaultPageSize; }
    public void setDefaultPageSize(int defaultPageSize) { this.defaultPageSize = defaultPageSize; }
    public int getMaxFeedSize() { return maxFeedSize; }
    public void setMaxFeedSize(int maxFeedSize) { this.maxFeedSize = maxFeedSize; }
}
