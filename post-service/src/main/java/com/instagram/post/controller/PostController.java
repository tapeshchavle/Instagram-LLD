package com.instagram.post.controller;

import com.instagram.common.dto.request.CreatePostRequest;
import com.instagram.common.dto.response.PostResponse;
import com.instagram.post.service.MediaService;
import com.instagram.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for post and media operations.
 */
@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;
    private final MediaService mediaService;

    public PostController(PostService postService, MediaService mediaService) {
        this.postService = postService;
        this.mediaService = mediaService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable String postId,
                                                           @RequestParam String userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(Map.of("message", "Post deleted"));
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable String userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId, page, size));
    }

    @PostMapping("/media/presigned-url")
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam String fileName,
                                                                @RequestParam String contentType) {
        String url = mediaService.generatePresignedUrl(fileName, contentType);
        return ResponseEntity.ok(Map.of("uploadUrl", url));
    }

    // ==================== Internal endpoints (called by other services) ====================

    @PostMapping("/posts/{postId}/increment-like")
    public ResponseEntity<Void> incrementLikeCount(@PathVariable String postId) {
        postService.incrementLikeCount(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/decrement-like")
    public ResponseEntity<Void> decrementLikeCount(@PathVariable String postId) {
        postService.decrementLikeCount(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/increment-comment")
    public ResponseEntity<Void> incrementCommentCount(@PathVariable String postId) {
        postService.incrementCommentCount(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/increment-share")
    public ResponseEntity<Void> incrementShareCount(@PathVariable String postId) {
        postService.incrementShareCount(postId);
        return ResponseEntity.ok().build();
    }
}
