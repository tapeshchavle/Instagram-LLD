package com.instagram.feed.service.impl;

import com.instagram.common.dto.response.FeedResponse;
import com.instagram.common.dto.response.PostResponse;
import com.instagram.common.event.PostCreatedEvent;
import com.instagram.feed.client.PostServiceClient;
import com.instagram.feed.client.UserServiceClient;
import com.instagram.feed.model.FeedItem;
import com.instagram.feed.service.FeedService;
import com.instagram.feed.strategy.FanOutOnReadStrategy;
import com.instagram.feed.strategy.FanOutOnWriteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of FeedService using the hybrid push/pull approach.
 *
 * Strategy Selection:
 * - Author has < CELEBRITY_THRESHOLD followers → FanOutOnWriteStrategy (push)
 * - Author has >= CELEBRITY_THRESHOLD followers → FanOutOnReadStrategy (pull)
 *
 * Feed Retrieval:
 * 1. Get pre-computed feed items (from push model) for normal users
 * 2. Dynamically fetch celebrity posts (from pull model)
 * 3. Merge, rank by recency, and paginate
 */
@Service
public class FeedServiceImpl implements FeedService {

    private static final Logger log = LoggerFactory.getLogger(FeedServiceImpl.class);

    private final FanOutOnWriteStrategy fanOutOnWriteStrategy;
    private final FanOutOnReadStrategy fanOutOnReadStrategy;
    private final UserServiceClient userServiceClient;
    private final PostServiceClient postServiceClient;

    @Value("${instagram.feed.celebrity-threshold:100000}")
    private long celebrityThreshold;

    public FeedServiceImpl(FanOutOnWriteStrategy fanOutOnWriteStrategy,
                           FanOutOnReadStrategy fanOutOnReadStrategy,
                           UserServiceClient userServiceClient,
                           PostServiceClient postServiceClient) {
        this.fanOutOnWriteStrategy = fanOutOnWriteStrategy;
        this.fanOutOnReadStrategy = fanOutOnReadStrategy;
        this.userServiceClient = userServiceClient;
        this.postServiceClient = postServiceClient;
    }

    @Override
    public FeedResponse getFeed(String userId, int page, int size) {
        log.info("Generating feed for user: {} (page={}, size={})", userId, page, size);

        Set<String> following = userServiceClient.getFollowingIds(userId);

        // 1. Get pre-computed feed from push model (normal users' posts)
        List<FeedItem> pushFeedItems = fanOutOnWriteStrategy.getFeed(userId, following, page, size);

        // 2. Get celebrity posts dynamically via pull model
        Set<String> celebrityFollowing = following.stream()
                .filter(userServiceClient::isCelebrity)
                .collect(Collectors.toSet());

        List<FeedItem> pullFeedItems = Collections.emptyList();
        if (!celebrityFollowing.isEmpty()) {
            pullFeedItems = fanOutOnReadStrategy.getFeed(userId, celebrityFollowing, page, size);
        }

        // 3. Merge and sort by timestamp (newest first)
        List<FeedItem> mergedFeed = new ArrayList<>();
        mergedFeed.addAll(pushFeedItems);
        mergedFeed.addAll(pullFeedItems);
        mergedFeed.sort(Comparator.comparing(FeedItem::getTimestamp).reversed());

        // 4. Deduplicate (same post might appear from push and pull)
        Set<String> seen = new HashSet<>();
        List<FeedItem> deduplicated = mergedFeed.stream()
                .filter(item -> seen.add(item.getPostId()))
                .limit(size)
                .collect(Collectors.toList());

        // 5. Fetch full post details
        List<PostResponse> postResponses = deduplicated.stream()
                .map(item -> postServiceClient.getPost(item.getPostId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 6. Build response
        FeedResponse response = new FeedResponse();
        response.setUserId(userId);
        response.setPosts(postResponses);
        response.setPage(page);
        response.setSize(postResponses.size());
        response.setHasNext(deduplicated.size() >= size);

        log.info("Feed generated for user: {} — {} posts (push={}, pull={})",
                userId, postResponses.size(), pushFeedItems.size(), pullFeedItems.size());

        return response;
    }

    @Override
    public void handleNewPost(PostCreatedEvent event) {
        log.info("Handling new post event: {} by user: {}", event.getPostId(), event.getUserId());

        Set<String> followerIds = userServiceClient.getFollowerIds(event.getUserId());
        boolean isCelebrity = event.getAuthorFollowerCount() >= celebrityThreshold
                || userServiceClient.isCelebrity(event.getUserId());

        if (isCelebrity) {
            log.info("Author {} is celebrity — using FanOutOnRead strategy", event.getUserId());
            fanOutOnReadStrategy.distributePost(event, followerIds);
        } else {
            log.info("Author {} is normal user — using FanOutOnWrite strategy", event.getUserId());
            fanOutOnWriteStrategy.distributePost(event, followerIds);
        }
    }
}
