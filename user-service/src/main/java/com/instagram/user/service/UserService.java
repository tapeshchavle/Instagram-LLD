package com.instagram.user.service;

import com.instagram.common.dto.request.UserRegistrationRequest;
import com.instagram.common.dto.response.UserProfileResponse;
import java.util.List;
import java.util.Set;

/**
 * Service interface for user management operations.
 * Follows Interface Segregation Principle — only user-related operations.
 */
public interface UserService {

    UserProfileResponse registerUser(UserRegistrationRequest request);

    UserProfileResponse getUserById(String userId);

    UserProfileResponse getUserByUsername(String username);

    UserProfileResponse updateProfile(String userId, UserRegistrationRequest request);

    boolean isCelebrity(String userId);

    void incrementPostCount(String userId);

    List<UserProfileResponse> searchUsers(String query);
}
