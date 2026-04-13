package com.instagram.user.service.impl;

import com.instagram.common.dto.response.UserProfileResponse;
import com.instagram.common.exception.DuplicateResourceException;
import com.instagram.common.exception.ResourceNotFoundException;
import com.instagram.user.model.FollowRelation;
import com.instagram.user.model.User;
import com.instagram.user.repository.FollowRepository;
import com.instagram.user.repository.UserRepository;
import com.instagram.user.service.FollowService;
import com.instagram.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of FollowService.
 * Manages follow/unfollow operations and maintains social graph.
 * Updates engagement scores for feed ranking.
 */
@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FollowServiceImpl(FollowRepository followRepository,
                             UserRepository userRepository,
                             UserService userService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void followUser(String followerId, String followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        // Validate both users exist
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", followerId));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", followeeId));

        if (followRepository.exists(followerId, followeeId)) {
            throw new DuplicateResourceException("Follow", followerId + " -> " + followeeId);
        }

        FollowRelation relation = new FollowRelation(
                UUID.randomUUID().toString(),
                followerId,
                followeeId
        );

        followRepository.save(relation);

        // Update atomic counters
        follower.incrementFollowingCount();
        followee.incrementFollowerCount();
    }

    @Override
    public void unfollowUser(String followerId, String followeeId) {
        if (!followRepository.exists(followerId, followeeId)) {
            throw new ResourceNotFoundException("Follow", followerId + " -> " + followeeId);
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", followerId));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", followeeId));

        followRepository.delete(followerId, followeeId);

        follower.decrementFollowingCount();
        followee.decrementFollowerCount();
    }

    @Override
    public Set<String> getFollowers(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return followRepository.getFollowers(userId);
    }

    @Override
    public Set<String> getFollowing(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return followRepository.getFollowing(userId);
    }

    @Override
    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.exists(followerId, followeeId);
    }

    @Override
    public List<UserProfileResponse> getFollowerProfiles(String userId) {
        Set<String> followerIds = getFollowers(userId);
        return followerIds.stream()
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfileResponse> getFollowingProfiles(String userId) {
        Set<String> followingIds = getFollowing(userId);
        return followingIds.stream()
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }
}
