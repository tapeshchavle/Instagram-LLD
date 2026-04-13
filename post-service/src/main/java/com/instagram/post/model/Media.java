package com.instagram.post.model;

import com.instagram.common.enums.MediaType;

/**
 * Represents a single media file (photo/video) within a post.
 * Actual files are stored in Object Storage (S3); this is metadata only.
 */
public class Media {

    private String id;
    private String postId;
    private String url;
    private MediaType mediaType;
    private int order;
    private int width;
    private int height;

    public Media() {}

    public Media(String id, String postId, String url, MediaType mediaType,
                 int order, int width, int height) {
        this.id = id;
        this.postId = postId;
        this.url = url;
        this.mediaType = mediaType;
        this.order = order;
        this.width = width;
        this.height = height;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public MediaType getMediaType() { return mediaType; }
    public void setMediaType(MediaType mediaType) { this.mediaType = mediaType; }
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}
