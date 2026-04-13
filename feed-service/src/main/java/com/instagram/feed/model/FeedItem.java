package com.instagram.feed.model;

import java.time.Instant;

/**
 * Represents a single item in a user's feed.
 * Stores only the post reference and metadata needed for ranking.
 */
public class FeedItem {

    private String postId;
    private String authorId;
    private Instant timestamp;
    private double score;

    public FeedItem() {}

    public FeedItem(String postId, String authorId, Instant timestamp, double score) {
        this.postId = postId;
        this.authorId = authorId;
        this.timestamp = timestamp;
        this.score = score;
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
