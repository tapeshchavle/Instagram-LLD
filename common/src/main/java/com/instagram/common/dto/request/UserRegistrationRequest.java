package com.instagram.common.dto.request;

/**
 * Request DTO for user registration.
 */
public class UserRegistrationRequest {

    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profilePictureUrl;

    public UserRegistrationRequest() {}

    public UserRegistrationRequest(String username, String email, String fullName, String bio, String profilePictureUrl) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

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
}
