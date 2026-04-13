package com.instagram.notification.factory;

import com.instagram.common.enums.NotificationType;
import com.instagram.notification.model.Notification;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Factory pattern for creating typed notifications.
 * Centralizes message formatting and notification construction.
 */
@Component
public class NotificationFactory {

    public Notification createLikeNotification(String userId, String actorId, String postId) {
        return new Notification(
                UUID.randomUUID().toString(),
                userId, actorId,
                NotificationType.LIKE,
                postId,
                actorId + " liked your post"
        );
    }

    public Notification createCommentNotification(String userId, String actorId,
                                                    String postId, String commentPreview) {
        String preview = commentPreview.length() > 50
                ? commentPreview.substring(0, 50) + "..."
                : commentPreview;
        return new Notification(
                UUID.randomUUID().toString(),
                userId, actorId,
                NotificationType.COMMENT,
                postId,
                actorId + " commented: " + preview
        );
    }

    public Notification createFollowNotification(String userId, String actorId) {
        return new Notification(
                UUID.randomUUID().toString(),
                userId, actorId,
                NotificationType.FOLLOW,
                actorId,
                actorId + " started following you"
        );
    }

    public Notification createMentionNotification(String userId, String actorId, String postId) {
        return new Notification(
                UUID.randomUUID().toString(),
                userId, actorId,
                NotificationType.MENTION,
                postId,
                actorId + " mentioned you in a post"
        );
    }
}
