package com.instagram.feed.strategy;

import com.instagram.common.event.PostCreatedEvent;
import com.instagram.feed.model.FeedItem;
import com.instagram.feed.repository.FeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Fan-out-on-write (Push Model) strategy.
 *
 * When a normal user (< 100K followers) creates a post, this strategy
 * immediately pushes the post into every follower's pre-computed feed cache.
 *
 * Pros:
 * - Super-fast reads (feed is already pre-computed)
 * - Works efficiently for small and medium accounts
 *
 * Cons:
 * - High write amplification for users with many followers
 * - Not suitable for celebrities (millions of writes per post)
 *
 * Equivalent Redis operation: LPUSH user:{followerId}:feed {postId}
 */
@Component
public class FanOutOnWriteStrategy implements FeedGenerationStrategy {

    private static final Logger log = LoggerFactory.getLogger(FanOutOnWriteStrategy.class);
    private final FeedRepository feedRepository;

    public FanOutOnWriteStrategy(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public void distributePost(PostCreatedEvent event, Set<String> followerIds) {
        log.info("[FanOutOnWrite] Distributing post {} to {} followers",
                event.getPostId(), followerIds.size());

        FeedItem feedItem = new FeedItem(
                event.getPostId(),
                event.getUserId(),
                event.getTimestamp() != null ? event.getTimestamp() : Instant.now(),
                1.0 // Default engagement score
        );

        // Push to every follower's feed cache
        for (String followerId : followerIds) {
            feedRepository.pushToFeed(followerId, feedItem);
        }

        // Also push to the author's own feed
        feedRepository.pushToFeed(event.getUserId(), feedItem);

        log.info("[FanOutOnWrite] Completed distribution for post {}", event.getPostId());
    }

    @Override
    public List<FeedItem> getFeed(String userId, Set<String> following, int page, int size) {
        // Feed is pre-computed — just read from cache
        return feedRepository.getFeed(userId, page, size);
    }

    @Override
    public String getStrategyName() {
        return "FAN_OUT_ON_WRITE";
    }
}
