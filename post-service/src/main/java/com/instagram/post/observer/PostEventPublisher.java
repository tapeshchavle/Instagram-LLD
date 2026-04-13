package com.instagram.post.observer;

import com.instagram.common.event.PostCreatedEvent;
import com.instagram.common.event.PostDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Publisher component that manages PostEventListeners and dispatches events.
 *
 * Design Pattern: Observer (Publisher/Subject)
 * - Maintains a list of listeners
 * - Notifies all registered listeners when a post event occurs
 * - Uses CopyOnWriteArrayList for thread-safe listener management
 *
 * In production, this would publish events to a Kafka topic.
 */
@Component
public class PostEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PostEventPublisher.class);
    private final List<PostEventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(PostEventListener listener) {
        listeners.add(listener);
        log.info("Registered post event listener: {}", listener.getClass().getSimpleName());
    }

    public void unregisterListener(PostEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that a new post was created.
     * Each listener handles the event independently (feed update, search indexing, etc.)
     */
    public void publishPostCreated(PostCreatedEvent event) {
        log.info("Publishing PostCreatedEvent for post: {} by user: {}",
                event.getPostId(), event.getUserId());
        for (PostEventListener listener : listeners) {
            try {
                listener.onPostCreated(event);
            } catch (Exception e) {
                log.error("Error notifying listener {} for post creation: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Notifies all listeners that a post was deleted.
     */
    public void publishPostDeleted(PostDeletedEvent event) {
        log.info("Publishing PostDeletedEvent for post: {}", event.getPostId());
        for (PostEventListener listener : listeners) {
            try {
                listener.onPostDeleted(event);
            } catch (Exception e) {
                log.error("Error notifying listener {} for post deletion: {}",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
