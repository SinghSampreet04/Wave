package com.wave.backend.notification.dto;

import com.wave.backend.notification.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;

    private NotificationType type;

    private String title;

    private String body;

    private Long referenceId;

    private String referenceType;

    private boolean read;

    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(
            Long id,
            NotificationType type,
            String title,
            String body,
            Long referenceId,
            String referenceType,
            boolean read,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}