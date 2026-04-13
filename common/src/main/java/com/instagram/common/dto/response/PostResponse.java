package com.instagram.common.dto.response;

import com.instagram.common.enums.MediaType;
import com.instagram.common.enums.PostStatus;
import java.time.Instant;
import java.util.List;

/**
 * Response DTO for post details.
 */
public class PostResponse {

    private String postId;
    private String userId;
    private String username;
    private String caption;
    private List<MediaItem> media;
    private List<String> hashtags;
    private PostStatus status;
    private long likeCount;
    private long commentCount;
    private long shareCount;
    private Instant createdAt;

    public PostResponse() {}

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public List<MediaItem> getMedia() { return media; }
    public void setMedia(List<MediaItem> media) { this.media = media; }
    public List<String> getHashtags() { return hashtags; }
    public void setHashtags(List<String> hashtags) { this.hashtags = hashtags; }
    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }
    public long getLikeCount() { return likeCount; }
    public void setLikeCount(long likeCount) { this.likeCount = likeCount; }
    public long getCommentCount() { return commentCount; }
    public void setCommentCount(long commentCount) { this.commentCount = commentCount; }
    public long getShareCount() { return shareCount; }
    public void setShareCount(long shareCount) { this.shareCount = shareCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    /**
     * Media item within a post response.
     */
    public static class MediaItem {
        private String mediaId;
        private String url;
        private MediaType mediaType;
        private int order;

        public MediaItem() {}

        public String getMediaId() { return mediaId; }
        public void setMediaId(String mediaId) { this.mediaId = mediaId; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public MediaType getMediaType() { return mediaType; }
        public void setMediaType(MediaType mediaType) { this.mediaType = mediaType; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
    }
}
