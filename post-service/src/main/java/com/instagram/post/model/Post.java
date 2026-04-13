package com.instagram.post.model;

import com.instagram.common.enums.PostStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Domain entity representing a post.
 * Uses the Builder pattern for flexible construction supporting
 * single photo, multi-photo carousel, and video posts.
 */
public class Post {

    private final String id;
    private final String userId;
    private String caption;
    private List<Media> mediaList;
    private List<String> hashtags;
    private PostStatus status;
    private final AtomicLong likeCount;
    private final AtomicLong commentCount;
    private final AtomicLong shareCount;
    private final Instant createdAt;

    private Post(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.caption = builder.caption;
        this.mediaList = builder.mediaList;
        this.hashtags = builder.hashtags;
        this.status = builder.status;
        this.likeCount = new AtomicLong(0);
        this.commentCount = new AtomicLong(0);
        this.shareCount = new AtomicLong(0);
        this.createdAt = Instant.now();
    }

    // --- Atomic counter operations ---
    public void incrementLikeCount() { likeCount.incrementAndGet(); }
    public void decrementLikeCount() { likeCount.decrementAndGet(); }
    public void incrementCommentCount() { commentCount.incrementAndGet(); }
    public void incrementShareCount() { shareCount.incrementAndGet(); }

    // --- Getters ---
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public List<Media> getMediaList() { return mediaList; }
    public List<String> getHashtags() { return hashtags; }
    public void setHashtags(List<String> hashtags) { this.hashtags = hashtags; }
    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }
    public long getLikeCount() { return likeCount.get(); }
    public long getCommentCount() { return commentCount.get(); }
    public long getShareCount() { return shareCount.get(); }
    public Instant getCreatedAt() { return createdAt; }

    /**
     * Builder pattern for Post construction.
     * Allows flexible creation of different post types.
     *
     * Usage:
     *   Post post = new Post.Builder("postId", "userId")
     *       .caption("My amazing photo")
     *       .addMedia(media1)
     *       .addMedia(media2)
     *       .hashtags(List.of("travel", "photography"))
     *       .status(PostStatus.PUBLISHED)
     *       .build();
     */
    public static class Builder {
        private final String id;
        private final String userId;
        private String caption = "";
        private List<Media> mediaList = new ArrayList<>();
        private List<String> hashtags = new ArrayList<>();
        private PostStatus status = PostStatus.PUBLISHED;

        public Builder(String id, String userId) {
            this.id = id;
            this.userId = userId;
        }

        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder addMedia(Media media) {
            this.mediaList.add(media);
            return this;
        }

        public Builder mediaList(List<Media> mediaList) {
            this.mediaList = new ArrayList<>(mediaList);
            return this;
        }

        public Builder hashtags(List<String> hashtags) {
            this.hashtags = new ArrayList<>(hashtags);
            return this;
        }

        public Builder status(PostStatus status) {
            this.status = status;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }
}
