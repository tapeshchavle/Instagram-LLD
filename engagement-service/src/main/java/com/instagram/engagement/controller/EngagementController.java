package com.instagram.engagement.controller;

import com.instagram.common.dto.request.CreateCommentRequest;
import com.instagram.common.dto.response.CommentResponse;
import com.instagram.engagement.service.EngagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for engagement operations (likes, comments, shares).
 */
@RestController
@RequestMapping("/api/v1/posts/{postId}")
public class EngagementController {

    private final EngagementService engagementService;

    public EngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @PostMapping("/likes")
    public ResponseEntity<Map<String, String>> likePost(@PathVariable String postId,
                                                         @RequestParam String userId) {
        engagementService.likePost(userId, postId);
        return ResponseEntity.ok(Map.of("message", "Post liked"));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Map<String, String>> unlikePost(@PathVariable String postId,
                                                           @RequestParam String userId) {
        engagementService.unlikePost(userId, postId);
        return ResponseEntity.ok(Map.of("message", "Post unliked"));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable String postId,
                                                       @RequestBody CreateCommentRequest request) {
        request.setPostId(postId);
        CommentResponse response = engagementService.addComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String postId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(engagementService.getComments(postId, page, size));
    }

    @PostMapping("/shares")
    public ResponseEntity<Map<String, String>> sharePost(@PathVariable String postId,
                                                          @RequestParam String userId,
                                                          @RequestParam String sharedToUserId) {
        engagementService.sharePost(userId, postId, sharedToUserId);
        return ResponseEntity.ok(Map.of("message", "Post shared"));
    }
}
