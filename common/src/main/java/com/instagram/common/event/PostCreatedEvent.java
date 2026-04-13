package com.instagram.common.event;

import java.time.Instant;

/**
 * Event fired when a new post is created.
 * Consumed by Feed Service (fan-out), Search Service (indexing),
 * and Notification Service (notify followers).
 */
public class PostCreatedEvent {

    private String postId;
    private String userId;
    private String caption;
    private long authorFollowerCount;
    private Instant timestamp;

    public PostCreatedEvent() {}

    public PostCreatedEvent(String postId, String userId, String caption, long authorFollowerCount) {
        this.postId = postId;
        this.userId = userId;
        this.caption = caption;
        this.authorFollowerCount = authorFollowerCount;
        this.timestamp = Instant.now();
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public long getAuthorFollowerCount() { return authorFollowerCount; }
    public void setAuthorFollowerCount(long authorFollowerCount) { this.authorFollowerCount = authorFollowerCount; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
