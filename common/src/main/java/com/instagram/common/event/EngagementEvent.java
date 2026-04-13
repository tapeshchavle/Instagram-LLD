package com.instagram.common.event;

import com.instagram.common.enums.EngagementType;
import java.time.Instant;

/**
 * Event fired when a user engages with a post (like, comment, share).
 * Consumed by Notification Service and Post Service (counter updates).
 */
public class EngagementEvent {

    private String engagementId;
    private EngagementType type;
    private String userId;
    private String postId;
    private String postOwnerId;
    private String content; // For comments
    private Instant timestamp;

    public EngagementEvent() {}

    public EngagementEvent(String engagementId, EngagementType type, String userId,
                           String postId, String postOwnerId, String content) {
        this.engagementId = engagementId;
        this.type = type;
        this.userId = userId;
        this.postId = postId;
        this.postOwnerId = postOwnerId;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public String getEngagementId() { return engagementId; }
    public void setEngagementId(String engagementId) { this.engagementId = engagementId; }
    public EngagementType getType() { return type; }
    public void setType(EngagementType type) { this.type = type; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getPostOwnerId() { return postOwnerId; }
    public void setPostOwnerId(String postOwnerId) { this.postOwnerId = postOwnerId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
