package com.instagram.user.service.impl;

import com.instagram.common.dto.request.UserRegistrationRequest;
import com.instagram.common.dto.response.UserProfileResponse;
import com.instagram.common.enums.UserRole;
import com.instagram.common.exception.DuplicateResourceException;
import com.instagram.common.exception.ResourceNotFoundException;
import com.instagram.user.config.UserServiceConfig;
import com.instagram.user.model.User;
import com.instagram.user.repository.UserRepository;
import com.instagram.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of UserService.
 * Handles user registration, profile management, and celebrity detection.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserServiceConfig config;

    public UserServiceImpl(UserRepository userRepository, UserServiceConfig config) {
        this.userRepository = userRepository;
        this.config = config;
    }

    @Override
    public UserProfileResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email", request.getEmail());
        }

        User user = new User(
                UUID.randomUUID().toString(),
                request.getUsername(),
                request.getEmail(),
                request.getFullName(),
                request.getBio(),
                request.getProfilePictureUrl()
        );

        userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public UserProfileResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return toResponse(user);
    }

    @Override
    public UserProfileResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
        return toResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(String userId, UserRegistrationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());

        userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public boolean isCelebrity(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return user.getFollowerCount() >= config.getCelebrityThreshold()
                || user.getRole() == UserRole.CELEBRITY;
    }

    @Override
    public void incrementPostCount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.incrementPostCount();
    }

    @Override
    public List<UserProfileResponse> searchUsers(String query) {
        return userRepository.searchByUsernamePrefix(query).stream()
                .map(this::toResponse)
                .toList();
    }

    private UserProfileResponse toResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setBio(user.getBio());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setRole(user.getRole());
        response.setFollowerCount(user.getFollowerCount());
        response.setFollowingCount(user.getFollowingCount());
        response.setPostCount(user.getPostCount());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
