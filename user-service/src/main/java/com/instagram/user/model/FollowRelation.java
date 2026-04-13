package com.instagram.user.model;

import java.time.Instant;

/**
 * Represents a follow relationship between two users.
 * Includes an engagement score to rank posts in the follower's feed.
 */
public class FollowRelation {

    private String id;
    private String followerId;
    private String followeeId;
    private double engagementScore;
    private final Instant createdAt;

    public FollowRelation(String id, String followerId, String followeeId) {
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.engagementScore = 1.0;
        this.createdAt = Instant.now();
    }

    /**
     * Boosts the engagement score when the follower interacts with the followee's content.
     */
    public void boostEngagement(double delta) {
        this.engagementScore = Math.min(10.0, this.engagementScore + delta);
    }

    /**
     * Decays the engagement score over time for less active relationships.
     */
    public void decayEngagement(double factor) {
        this.engagementScore = Math.max(0.1, this.engagementScore * factor);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFollowerId() { return followerId; }
    public String getFolloweeId() { return followeeId; }
    public double getEngagementScore() { return engagementScore; }
    public void setEngagementScore(double engagementScore) { this.engagementScore = engagementScore; }
    public Instant getCreatedAt() { return createdAt; }
}
