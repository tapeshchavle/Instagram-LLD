package com.instagram.notification.service;

import com.instagram.common.event.EngagementEvent;
import com.instagram.common.event.UserFollowedEvent;
import com.instagram.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications(String userId, int page, int size);

    void markAsRead(String notificationId);

    long getUnreadCount(String userId);

    void handleEngagementEvent(EngagementEvent event);

    void handleFollowEvent(UserFollowedEvent event);
}
