package com.instagram.feed.strategy;

import com.instagram.common.dto.response.PostResponse;
import com.instagram.common.event.PostCreatedEvent;
import com.instagram.feed.client.PostServiceClient;
import com.instagram.feed.model.FeedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fan-out-on-read (Pull Model) strategy.
 *
 * For celebrities (> 100K followers), instead of pushing to millions of feeds,
 * we store the post in a celebrity timeline and merge it dynamically at read time.
 *
 * Pros:
 * - Avoids massive write amplification for celebrities
 * - Ensures fresh data when users request feeds
 *
 * Cons:
 * - Slightly higher read latency than push model
 * - Requires merge logic at read time
 *
 * In production, celebrity timelines would be in Redis hot cache.
 */
@Component
public class FanOutOnReadStrategy implements FeedGenerationStrategy {

    private static final Logger log = LoggerFactory.getLogger(FanOutOnReadStrategy.class);
    private final PostServiceClient postServiceClient;

    // Celebrity userId -> List of their recent post IDs (simulates Redis sorted set)
    private final Map<String, List<FeedItem>> celebrityTimelines = new ConcurrentHashMap<>();

    public FanOutOnReadStrategy(PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public void distributePost(PostCreatedEvent event, Set<String> followerIds) {
        // Instead of pushing to every follower, store in celebrity's timeline
        log.info("[FanOutOnRead] Storing post {} in celebrity timeline for user {}",
                event.getPostId(), event.getUserId());

        FeedItem item = new FeedItem(
                event.getPostId(),
                event.getUserId(),
                event.getTimestamp(),
                1.0
        );

        celebrityTimelines.computeIfAbsent(event.getUserId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(0, item); // newest first

        // Trim to keep only recent posts
        List<FeedItem> timeline = celebrityTimelines.get(event.getUserId());
        synchronized (timeline) {
            while (timeline.size() > 100) {
                timeline.remove(timeline.size() - 1);
            }
        }
    }

    @Override
    public List<FeedItem> getFeed(String userId, Set<String> followedCelebrities, int page, int size) {
        // At read time, dynamically fetch posts from all celebrities the user follows
        log.info("[FanOutOnRead] Fetching celebrity posts for user {} from {} celebrities",
                userId, followedCelebrities.size());

        List<FeedItem> merged = new ArrayList<>();

        for (String celebrityId : followedCelebrities) {
            List<FeedItem> timeline = celebrityTimelines.getOrDefault(celebrityId, Collections.emptyList());
            synchronized (timeline) {
                merged.addAll(new ArrayList<>(timeline));
            }
        }

        // Sort by timestamp (newest first)
        merged.sort(Comparator.comparing(FeedItem::getTimestamp).reversed());

        // Paginate
        int start = page * size;
        int end = Math.min(start + size, merged.size());
        if (start >= merged.size()) return Collections.emptyList();

        return merged.subList(start, end);
    }

    @Override
    public String getStrategyName() {
        return "FAN_OUT_ON_READ";
    }
}
