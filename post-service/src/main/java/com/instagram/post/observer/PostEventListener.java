package com.instagram.post.observer;

import com.instagram.common.event.PostCreatedEvent;
import com.instagram.common.event.PostDeletedEvent;

/**
 * Observer interface for post lifecycle events.
 * Implementing classes react to post creation/deletion
 * without the Post Service knowing their implementation details.
 *
 * Design Pattern: Observer
 * - Decouples post creation from feed generation, search indexing, and notifications.
 * - In production, this would be replaced by Kafka event consumers.
 */
public interface PostEventListener {

    void onPostCreated(PostCreatedEvent event);

    void onPostDeleted(PostDeletedEvent event);
}
