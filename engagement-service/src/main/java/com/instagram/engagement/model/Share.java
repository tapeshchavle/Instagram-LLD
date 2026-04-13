package com.instagram.engagement.model;

import java.time.Instant;

/**
 * Represents a share of a post to another user.
 */
public class Share {

    private String id;
    private String userId;
    private String postId;
    private String sharedToUserId;
    private final Instant createdAt;

    public Share(String id, String userId, String postId, String sharedToUserId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.sharedToUserId = sharedToUserId;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getPostId() { return postId; }
    public String getSharedToUserId() { return sharedToUserId; }
    public Instant getCreatedAt() { return createdAt; }
}
