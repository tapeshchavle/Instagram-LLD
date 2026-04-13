package com.instagram.common.dto.response;

import com.instagram.common.enums.UserRole;
import java.time.Instant;

/**
 * Response DTO for user profile information.
 */
public class UserProfileResponse {

    private String userId;
    private String username;
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private UserRole role;
    private long followerCount;
    private long followingCount;
    private long postCount;
    private Instant createdAt;

    public UserProfileResponse() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public long getFollowerCount() { return followerCount; }
    public void setFollowerCount(long followerCount) { this.followerCount = followerCount; }
    public long getFollowingCount() { return followingCount; }
    public void setFollowingCount(long followingCount) { this.followingCount = followingCount; }
    public long getPostCount() { return postCount; }
    public void setPostCount(long postCount) { this.postCount = postCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
