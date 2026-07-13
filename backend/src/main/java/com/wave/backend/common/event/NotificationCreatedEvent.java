package com.wave.backend.common.event;

import com.wave.backend.notification.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationCreatedEvent {

    private final Long notificationId;

    private final Long recipientId;

    private final NotificationType type;

    private final String title;

    private final String body;

    private final Long referenceId;

    private final String referenceType;

    private final LocalDateTime createdAt;

    public NotificationCreatedEvent(
            Long notificationId,
            Long recipientId,
            NotificationType type,
            String title,
            String body,
            Long referenceId,
            String referenceType,
            LocalDateTime createdAt
    ) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.createdAt = createdAt;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}