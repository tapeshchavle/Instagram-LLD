package com.instagram.search.controller;

import com.instagram.common.dto.response.SearchResponse;
import com.instagram.common.event.PostCreatedEvent;
import com.instagram.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/users")
    public ResponseEntity<SearchResponse> searchUsers(@RequestParam String q,
                                                       @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(searchService.searchUsers(q, limit));
    }

    @GetMapping("/hashtags")
    public ResponseEntity<SearchResponse> searchHashtags(@RequestParam String q,
                                                          @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(searchService.searchHashtags(q, limit));
    }

    @GetMapping("/posts")
    public ResponseEntity<SearchResponse> searchPosts(@RequestParam String q,
                                                       @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(searchService.searchPosts(q, limit));
    }

    /**
     * Internal endpoint: Receives new post events from Post Service for indexing.
     */
    @PostMapping("/index")
    public ResponseEntity<Map<String, String>> indexContent(@RequestBody PostCreatedEvent event) {
        searchService.indexPost(event.getPostId(), event.getUserId(), event.getCaption());
        return ResponseEntity.ok(Map.of("status", "indexed", "postId", event.getPostId()));
    }
}
