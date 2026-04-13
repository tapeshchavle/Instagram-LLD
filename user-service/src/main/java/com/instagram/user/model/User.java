package com.instagram.user.model;

import com.instagram.common.enums.UserRole;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Domain entity representing a user account.
 * Contains profile information and social metrics.
 */
public class User {

    private String id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private UserRole role;
    private final AtomicLong followerCount;
    private final AtomicLong followingCount;
    private final AtomicLong postCount;
    private final Instant createdAt;

    public User(String id, String username, String email, String fullName,
                String bio, String profilePictureUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.role = UserRole.NORMAL;
        this.followerCount = new AtomicLong(0);
        this.followingCount = new AtomicLong(0);
        this.postCount = new AtomicLong(0);
        this.createdAt = Instant.now();
    }

    // --- Atomic counter operations ---
    public void incrementFollowerCount() { this.followerCount.incrementAndGet(); }
    public void decrementFollowerCount() { this.followerCount.decrementAndGet(); }
    public void incrementFollowingCount() { this.followingCount.incrementAndGet(); }
    public void decrementFollowingCount() { this.followingCount.decrementAndGet(); }
    public void incrementPostCount() { this.postCount.incrementAndGet(); }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public long getFollowerCount() { return followerCount.get(); }
    public long getFollowingCount() { return followingCount.get(); }
    public long getPostCount() { return postCount.get(); }
    public Instant getCreatedAt() { return createdAt; }
}
