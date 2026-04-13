package com.instagram.common.dto.request;

import com.instagram.common.enums.MediaType;
import java.util.List;

/**
 * Request DTO for creating a new post.
 * Supports carousel posts with multiple media items.
 */
public class CreatePostRequest {

    private String userId;
    private String caption;
    private List<MediaItem> mediaItems;

    public CreatePostRequest() {}

    public CreatePostRequest(String userId, String caption, List<MediaItem> mediaItems) {
        this.userId = userId;
        this.caption = caption;
        this.mediaItems = mediaItems;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public List<MediaItem> getMediaItems() { return mediaItems; }
    public void setMediaItems(List<MediaItem> mediaItems) { this.mediaItems = mediaItems; }

    /**
     * Represents a single media item within a post.
     */
    public static class MediaItem {
        private String url;
        private MediaType mediaType;
        private int width;
        private int height;

        public MediaItem() {}

        public MediaItem(String url, MediaType mediaType, int width, int height) {
            this.url = url;
            this.mediaType = mediaType;
            this.width = width;
            this.height = height;
        }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public MediaType getMediaType() { return mediaType; }
        public void setMediaType(MediaType mediaType) { this.mediaType = mediaType; }
        public int getWidth() { return width; }
        public void setWidth(int width) { this.width = width; }
        public int getHeight() { return height; }
        public void setHeight(int height) { this.height = height; }
    }
}
