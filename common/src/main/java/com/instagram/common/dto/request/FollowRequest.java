package com.instagram.common.dto.request;

/**
 * Request DTO for follow/unfollow operations.
 */
public class FollowRequest {

    private String followerId;
    private String followeeId;

    public FollowRequest() {}

    public FollowRequest(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public String getFollowerId() { return followerId; }
    public void setFollowerId(String followerId) { this.followerId = followerId; }
    public String getFolloweeId() { return followeeId; }
    public void setFolloweeId(String followeeId) { this.followeeId = followeeId; }
}
