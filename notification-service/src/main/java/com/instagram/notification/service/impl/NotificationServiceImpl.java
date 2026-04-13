package com.instagram.notification.service.impl;

import com.instagram.common.enums.EngagementType;
import com.instagram.common.event.EngagementEvent;
import com.instagram.common.event.UserFollowedEvent;
import com.instagram.common.exception.ResourceNotFoundException;
import com.instagram.notification.factory.NotificationFactory;
import com.instagram.notification.model.Notification;
import com.instagram.notification.repository.NotificationRepository;
import com.instagram.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final NotificationRepository notificationRepository;
    private final NotificationFactory notificationFactory;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                    NotificationFactory notificationFactory) {
        this.notificationRepository = notificationRepository;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public List<Notification> getNotifications(String userId, int page, int size) {
        return notificationRepository.findByUserId(userId, page, size);
    }

    @Override
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        notification.setRead(true);
    }

    @Override
    public long getUnreadCount(String userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void handleEngagementEvent(EngagementEvent event) {
        Notification notification;
        if (event.getType() == EngagementType.LIKE) {
            notification = notificationFactory.createLikeNotification(
                    event.getPostOwnerId(), event.getUserId(), event.getPostId());
        } else if (event.getType() == EngagementType.COMMENT) {
            notification = notificationFactory.createCommentNotification(
                    event.getPostOwnerId(), event.getUserId(), event.getPostId(),
                    event.getContent() != null ? event.getContent() : "");
        } else {
            return;
        }

        notificationRepository.save(notification);
        log.info("Created {} notification for user {}", event.getType(), event.getPostOwnerId());
    }

    @Override
    public void handleFollowEvent(UserFollowedEvent event) {
        Notification notification = notificationFactory.createFollowNotification(
                event.getFolloweeId(), event.getFollowerId());
        notificationRepository.save(notification);
        log.info("Created FOLLOW notification for user {}", event.getFolloweeId());
    }
}
