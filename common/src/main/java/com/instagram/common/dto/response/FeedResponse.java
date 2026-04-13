package com.instagram.common.dto.response;

import java.util.List;

/**
 * Response DTO for paginated feed.
 */
public class FeedResponse {

    private String userId;
    private List<PostResponse> posts;
    private int page;
    private int size;
    private boolean hasNext;

    public FeedResponse() {}

    public FeedResponse(String userId, List<PostResponse> posts, int page, int size, boolean hasNext) {
        this.userId = userId;
        this.posts = posts;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<PostResponse> getPosts() { return posts; }
    public void setPosts(List<PostResponse> posts) { this.posts = posts; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
}
