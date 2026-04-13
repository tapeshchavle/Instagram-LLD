package com.instagram.common.event;

import java.time.Instant;

/**
 * Event fired when a user follows another user.
 * Consumed by Notification Service and Feed Service.
 */
public class UserFollowedEvent {

    private String followerId;
    private String followeeId;
    private Instant timestamp;

    public UserFollowedEvent() {}

    public UserFollowedEvent(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.timestamp = Instant.now();
    }

    public String getFollowerId() { return followerId; }
    public void setFollowerId(String followerId) { this.followerId = followerId; }
    public String getFolloweeId() { return followeeId; }
    public void setFolloweeId(String followeeId) { this.followeeId = followeeId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
