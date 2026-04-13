package com.instagram.common.event;

import java.time.Instant;

/**
 * Event fired when a post is deleted.
 * Consumed by Feed Service (remove from feeds) and Search Service (remove from index).
 */
public class PostDeletedEvent {

    private String postId;
    private String userId;
    private Instant timestamp;

    public PostDeletedEvent() {}

    public PostDeletedEvent(String postId, String userId) {
        this.postId = postId;
        this.userId = userId;
        this.timestamp = Instant.now();
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
