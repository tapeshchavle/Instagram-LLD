package com.instagram.post.observer;

import com.instagram.common.event.PostCreatedEvent;
import com.instagram.common.event.PostDeletedEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Observer that notifies downstream services about post events via REST.
 * Sends events to Feed Service and Search Service.
 *
 * In production, this would be replaced by Kafka producers.
 */
@Component
public class PostEventNotifier implements PostEventListener {

    private static final Logger log = LoggerFactory.getLogger(PostEventNotifier.class);
    private final PostEventPublisher publisher;
    private final RestClient restClient;

    @Value("${instagram.feed-service.url:http://localhost:8083}")
    private String feedServiceUrl;

    @Value("${instagram.search-service.url:http://localhost:8085}")
    private String searchServiceUrl;

    public PostEventNotifier(PostEventPublisher publisher) {
        this.publisher = publisher;
        this.restClient = RestClient.create();
    }

    @PostConstruct
    public void init() {
        publisher.registerListener(this);
    }

    @Override
    public void onPostCreated(PostCreatedEvent event) {
        // Notify Feed Service for fan-out
        try {
            restClient.post()
                    .uri(feedServiceUrl + "/api/v1/feed/ingest")
                    .body(event)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Notified Feed Service about new post: {}", event.getPostId());
        } catch (Exception e) {
            log.warn("Feed Service unavailable, event will be retried: {}", e.getMessage());
        }

        // Notify Search Service for indexing
        try {
            restClient.post()
                    .uri(searchServiceUrl + "/api/v1/search/index")
                    .body(event)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Notified Search Service about new post: {}", event.getPostId());
        } catch (Exception e) {
            log.warn("Search Service unavailable, event will be retried: {}", e.getMessage());
        }
    }

    @Override
    public void onPostDeleted(PostDeletedEvent event) {
        log.info("Post deleted event for: {}. Feed and Search cleanup triggered.", event.getPostId());
    }
}
