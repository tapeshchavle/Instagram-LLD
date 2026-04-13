package com.instagram.common.dto.request;

/**
 * Request DTO for creating a comment on a post.
 * Supports threaded replies via parentCommentId.
 */
public class CreateCommentRequest {

    private String userId;
    private String postId;
    private String content;
    private String parentCommentId;

    public CreateCommentRequest() {}

    public CreateCommentRequest(String userId, String postId, String content, String parentCommentId) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(String parentCommentId) { this.parentCommentId = parentCommentId; }
}
