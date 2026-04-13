package com.instagram.common.dto.response;

import java.util.List;

/**
 * Response DTO for search results.
 */
public class SearchResponse {

    private String query;
    private String type;
    private List<UserProfileResponse> users;
    private List<PostResponse> posts;
    private List<HashtagResult> hashtags;
    private long totalResults;

    public SearchResponse() {}

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<UserProfileResponse> getUsers() { return users; }
    public void setUsers(List<UserProfileResponse> users) { this.users = users; }
    public List<PostResponse> getPosts() { return posts; }
    public void setPosts(List<PostResponse> posts) { this.posts = posts; }
    public List<HashtagResult> getHashtags() { return hashtags; }
    public void setHashtags(List<HashtagResult> hashtags) { this.hashtags = hashtags; }
    public long getTotalResults() { return totalResults; }
    public void setTotalResults(long totalResults) { this.totalResults = totalResults; }

    /**
     * A single hashtag search result.
     */
    public static class HashtagResult {
        private String tag;
        private long postCount;

        public HashtagResult() {}

        public HashtagResult(String tag, long postCount) {
            this.tag = tag;
            this.postCount = postCount;
        }

        public String getTag() { return tag; }
        public void setTag(String tag) { this.tag = tag; }
        public long getPostCount() { return postCount; }
        public void setPostCount(long postCount) { this.postCount = postCount; }
    }
}
