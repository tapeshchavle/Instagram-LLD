package com.instagram.engagement.factory;

import com.instagram.common.enums.EngagementType;
import com.instagram.engagement.model.Comment;
import com.instagram.engagement.model.Like;
import com.instagram.engagement.model.Share;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Factory pattern for creating engagement objects.
 *
 * Design Pattern: Factory
 * - Centralizes object creation logic for different engagement types
 * - Encapsulates ID generation and default values
 * - Makes it easy to add new engagement types (e.g., Reaction, Bookmark)
 */
@Component
public class EngagementFactory {

    public Like createLike(String userId, String postId) {
        return new Like(UUID.randomUUID().toString(), userId, postId);
    }

    public Comment createComment(String userId, String postId, String content, String parentCommentId) {
        return new Comment(UUID.randomUUID().toString(), postId, userId, content, parentCommentId);
    }

    public Share createShare(String userId, String postId, String sharedToUserId) {
        return new Share(UUID.randomUUID().toString(), userId, postId, sharedToUserId);
    }

    /**
     * Returns the engagement type for a given action string.
     */
    public EngagementType resolveType(String action) {
        return switch (action.toUpperCase()) {
            case "LIKE" -> EngagementType.LIKE;
            case "COMMENT" -> EngagementType.COMMENT;
            case "SHARE" -> EngagementType.SHARE;
            default -> throw new IllegalArgumentException("Unknown engagement type: " + action);
        };
    }
}
