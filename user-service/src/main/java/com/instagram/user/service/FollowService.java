package com.instagram.user.service;

import com.instagram.common.dto.response.UserProfileResponse;
import java.util.List;
import java.util.Set;

/**
 * Service interface for follow/unfollow operations.
 * Separated from UserService per Interface Segregation Principle.
 */
public interface FollowService {

    void followUser(String followerId, String followeeId);

    void unfollowUser(String followerId, String followeeId);

    Set<String> getFollowers(String userId);

    Set<String> getFollowing(String userId);

    boolean isFollowing(String followerId, String followeeId);

    List<UserProfileResponse> getFollowerProfiles(String userId);

    List<UserProfileResponse> getFollowingProfiles(String userId);
}
