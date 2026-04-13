package com.instagram.feed.controller;

import com.instagram.common.dto.response.FeedResponse;
import com.instagram.common.event.PostCreatedEvent;
import com.instagram.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for feed operations.
 */
@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    /**
     * Get personalized feed for a user.
     */
    @GetMapping
    public ResponseEntity<FeedResponse> getFeed(@RequestParam String userId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(feedService.getFeed(userId, page, size));
    }

    /**
     * Internal endpoint: Receives new post events from Post Service.
     * Triggers feed distribution using the appropriate strategy.
     */
    @PostMapping("/ingest")
    public ResponseEntity<Map<String, String>> ingestPost(@RequestBody PostCreatedEvent event) {
        feedService.handleNewPost(event);
        return ResponseEntity.ok(Map.of("status", "ingested", "postId", event.getPostId()));
    }
}
