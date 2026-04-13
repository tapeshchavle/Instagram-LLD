package com.instagram.engagement.model;

import java.time.Instant;

/**
 * Represents a like on a post.
 */
public class Like {

    private String id;
    private String userId;
    private String postId;
    private final Instant createdAt;

    public Like(String id, String userId, String postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getPostId() { return postId; }
    public Instant getCreatedAt() { return createdAt; }
}
