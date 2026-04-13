package com.instagram.notification.repository;

import com.instagram.notification.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NotificationRepository {

    private final Map<String, Notification> notificationsById = new ConcurrentHashMap<>();
    private final Map<String, List<Notification>> notificationsByUserId = new ConcurrentHashMap<>();

    public Notification save(Notification notification) {
        notificationsById.put(notification.getId(), notification);
        notificationsByUserId.computeIfAbsent(notification.getUserId(),
                k -> Collections.synchronizedList(new ArrayList<>())).add(0, notification);
        return notification;
    }

    public List<Notification> findByUserId(String userId, int page, int size) {
        List<Notification> notifications = notificationsByUserId.getOrDefault(userId, Collections.emptyList());
        int start = page * size;
        int end = Math.min(start + size, notifications.size());
        if (start >= notifications.size()) return Collections.emptyList();
        return new ArrayList<>(notifications.subList(start, end));
    }

    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(notificationsById.get(id));
    }

    public long countUnreadByUserId(String userId) {
        return notificationsByUserId.getOrDefault(userId, Collections.emptyList()).stream()
                .filter(n -> !n.isRead())
                .count();
    }
}
