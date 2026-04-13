package com.instagram.notification.model;

import com.instagram.common.enums.NotificationType;
import java.time.Instant;

public class Notification {

    private String id;
    private String userId;       // recipient
    private String actorId;      // who triggered the notification
    private NotificationType type;
    private String referenceId;  // postId, commentId, etc.
    private String message;
    private boolean read;
    private final Instant createdAt;

    public Notification(String id, String userId, String actorId,
                        NotificationType type, String referenceId, String message) {
        this.id = id;
        this.userId = userId;
        this.actorId = actorId;
        this.type = type;
        this.referenceId = referenceId;
        this.message = message;
        this.read = false;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getActorId() { return actorId; }
    public NotificationType getType() { return type; }
    public String getReferenceId() { return referenceId; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public Instant getCreatedAt() { return createdAt; }
}
