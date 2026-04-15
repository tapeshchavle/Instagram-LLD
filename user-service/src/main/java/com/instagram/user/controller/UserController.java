package com.instagram.user.controller;

import com.instagram.common.dto.request.UserRegistrationRequest;
import com.instagram.common.dto.response.UserProfileResponse;
import com.instagram.user.service.FollowService;
import com.instagram.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REST controller for user management and social graph operations.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    public UserController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    // ==================== User CRUD ====================

    @PostMapping
    public ResponseEntity<UserProfileResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        UserProfileResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable String userId,
                                                              @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    // ==================== Follow Operations ====================

    @PostMapping("/{followeeId}/follow")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable String followeeId,
                                                          @RequestParam String followerId) {
        followService.followUser(followerId, followeeId);
        return ResponseEntity.ok(Map.of("message", "Followed successfully"));
    }

    @DeleteMapping("/{followeeId}/follow")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable String followeeId,
                                                             @RequestParam String followerId) {
        followService.unfollowUser(followerId, followeeId);
        return ResponseEntity.ok(Map.of("message", "Unfollowed successfully"));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserProfileResponse>> getFollowers(@PathVariable String userId) {
        return ResponseEntity.ok(followService.getFollowerProfiles(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserProfileResponse>> getFollowing(@PathVariable String userId) {
        return ResponseEntity.ok(followService.getFollowingProfiles(userId));
    }

    @GetMapping("/{userId}/followers/ids")
    public ResponseEntity<Set<String>> getFollowerIds(@PathVariable String userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following/ids")
    public ResponseEntity<Set<String>> getFollowingIds(@PathVariable String userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    // ==================== Internal Endpoints ====================

    @GetMapping("/{userId}/is-celebrity")
    public ResponseEntity<Map<String, Boolean>> isCelebrity(@PathVariable String userId) {
        return ResponseEntity.ok(Map.of("celebrity", userService.isCelebrity(userId)));
    }

    @PostMapping("/{userId}/increment-post-count")
    public ResponseEntity<Void> incrementPostCount(@PathVariable String userId) {
        userService.incrementPostCount(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserProfileResponse>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    //for the testing purpose
    @GetMapping("/hi")
    public String hi(){
        return "Hello from user-service";
    }
}
