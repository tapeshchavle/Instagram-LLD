package com.instagram.notification.controller;

import com.instagram.common.event.EngagementEvent;
import com.instagram.common.event.UserFollowedEvent;
import com.instagram.notification.model.Notification;
import com.instagram.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam String userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(notificationService.getNotifications(userId, page, size));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(Map.of("message", "Marked as read"));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam String userId) {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(userId)));
    }

    @PostMapping("/ingest/engagement")
    public ResponseEntity<Map<String, String>> ingestEngagement(@RequestBody EngagementEvent event) {
        notificationService.handleEngagementEvent(event);
        return ResponseEntity.ok(Map.of("status", "processed"));
    }

    @PostMapping("/ingest/follow")
    public ResponseEntity<Map<String, String>> ingestFollow(@RequestBody UserFollowedEvent event) {
        notificationService.handleFollowEvent(event);
        return ResponseEntity.ok(Map.of("status", "processed"));
    }
}
