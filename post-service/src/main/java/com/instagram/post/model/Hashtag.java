package com.instagram.post.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a hashtag used in posts.
 * Tracks usage count for trending/popularity ranking.
 */
public class Hashtag {

    private String id;
    private String tag;
    private final AtomicLong postCount;
    private final Instant createdAt;

    public Hashtag(String id, String tag) {
        this.id = id;
        this.tag = tag.toLowerCase().replaceAll("[^a-z0-9]", "");
        this.postCount = new AtomicLong(1);
        this.createdAt = Instant.now();
    }

    public void incrementPostCount() { postCount.incrementAndGet(); }

    public String getId() { return id; }
    public String getTag() { return tag; }
    public long getPostCount() { return postCount.get(); }
    public Instant getCreatedAt() { return createdAt; }
}
