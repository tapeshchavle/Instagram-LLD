package com.instagram.feed.service;

import com.instagram.common.dto.response.FeedResponse;
import com.instagram.common.event.PostCreatedEvent;

/**
 * Service interface for feed generation.
 */
public interface FeedService {

    /**
     * Returns a personalized feed for the given user.
     * Uses hybrid strategy: merges push (normal users) and pull (celebrity) feeds.
     */
    FeedResponse getFeed(String userId, int page, int size);

    /**
     * Handles incoming post creation event.
     * Selects appropriate strategy based on author's follower count.
     */
    void handleNewPost(PostCreatedEvent event);
}
