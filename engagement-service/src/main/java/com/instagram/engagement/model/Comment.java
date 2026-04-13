package com.instagram.engagement.model;

import java.time.Instant;

/**
 * Represents a comment on a post.
 * Supports threaded replies via parentCommentId.
 */
public class Comment {

    private String id;
    private String postId;
    private String userId;
    private String content;
    private String parentCommentId;
    private final Instant createdAt;

    public Comment(String id, String postId, String userId, String content, String parentCommentId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getParentCommentId() { return parentCommentId; }
    public Instant getCreatedAt() { return createdAt; }
}
