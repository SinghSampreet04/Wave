package com.wave.backend.notification.controller;

import com.wave.backend.notification.dto.NotificationResponse;
import com.wave.backend.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications() {

        return notificationService.getMyNotifications();

    }

    @GetMapping("/unread")
    public List<NotificationResponse> getUnreadNotifications() {

        return notificationService.getUnreadNotifications();

    }

    @GetMapping("/unread-count")
    public long getUnreadCount() {

        return notificationService.getUnreadCount();

    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(
            @PathVariable Long notificationId
    ) {

        notificationService.markAsRead(
                notificationId
        );

    }

}