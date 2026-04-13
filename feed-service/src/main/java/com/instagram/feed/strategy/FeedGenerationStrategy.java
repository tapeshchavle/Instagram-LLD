package com.instagram.feed.strategy;

import com.instagram.common.event.PostCreatedEvent;
import com.instagram.feed.model.FeedItem;

import java.util.List;
import java.util.Set;

/**
 * Strategy interface for feed generation.
 *
 * Design Pattern: Strategy
 * - FanOutOnWriteStrategy: Push model for normal users (pre-compute feeds)
 * - FanOutOnReadStrategy:  Pull model for celebrities (fetch at read time)
 *
 * The FeedService selects the appropriate strategy based on the author's
 * follower count, matching Instagram's real production architecture.
 */
public interface FeedGenerationStrategy {

    /**
     * Distributes a new post to appropriate feeds.
     *
     * @param event     The post creation event
     * @param followerIds The set of follower IDs for the post author
     */
    void distributePost(PostCreatedEvent event, Set<String> followerIds);

    /**
     * Retrieves feed items for a user.
     *
     * @param userId    The user requesting their feed
     * @param following The set of user IDs this user follows
     * @param page      Page number (0-indexed)
     * @param size      Items per page
     * @return List of feed items
     */
    List<FeedItem> getFeed(String userId, Set<String> following, int page, int size);

    /**
     * Returns the name of this strategy for logging/debugging.
     */
    String getStrategyName();
}
