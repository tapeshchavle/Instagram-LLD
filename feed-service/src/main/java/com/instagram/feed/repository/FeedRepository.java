package com.instagram.feed.repository;

import com.instagram.feed.model.FeedItem;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory feed cache.
 * Stores pre-computed feeds per user (simulates Redis LPUSH/LRANGE).
 * In production, this would be Redis with sorted sets.
 */
@Repository
public class FeedRepository {

    // userId -> Deque of FeedItems (newest first, simulates Redis list)
    private final Map<String, Deque<FeedItem>> feedCache = new ConcurrentHashMap<>();
    private static final int MAX_FEED_SIZE = 1000;

    /**
     * Pushes a feed item to the front of a user's feed (simulates Redis LPUSH).
     */
    public void pushToFeed(String userId, FeedItem item) {
        Deque<FeedItem> feed = feedCache.computeIfAbsent(userId, k -> new ArrayDeque<>());
        synchronized (feed) {
            feed.addFirst(item);
            // Trim to max size
            while (feed.size() > MAX_FEED_SIZE) {
                feed.removeLast();
            }
        }
    }

    /**
     * Gets a paginated slice of a user's feed (simulates Redis LRANGE).
     */
    public List<FeedItem> getFeed(String userId, int page, int size) {
        Deque<FeedItem> feed = feedCache.getOrDefault(userId, new ArrayDeque<>());
        List<FeedItem> feedList;
        synchronized (feed) {
            feedList = new ArrayList<>(feed);
        }

        int start = page * size;
        int end = Math.min(start + size, feedList.size());
        if (start >= feedList.size()) return Collections.emptyList();

        return feedList.subList(start, end);
    }

    /**
     * Removes a post from a user's feed.
     */
    public void removeFromFeed(String userId, String postId) {
        Deque<FeedItem> feed = feedCache.get(userId);
        if (feed != null) {
            synchronized (feed) {
                feed.removeIf(item -> item.getPostId().equals(postId));
            }
        }
    }

    /**
     * Gets the total number of items in a user's feed.
     */
    public int getFeedSize(String userId) {
        Deque<FeedItem> feed = feedCache.get(userId);
        return feed != null ? feed.size() : 0;
    }
}
