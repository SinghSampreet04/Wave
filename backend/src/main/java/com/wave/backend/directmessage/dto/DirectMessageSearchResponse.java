package com.wave.backend.directmessage.dto;

import java.time.LocalDateTime;

public class DirectMessageSearchResponse {

    private Long id;

    private Long senderId;

    private String senderUsername;

    private String content;

    private boolean edited;

    private boolean deleted;

    private LocalDateTime createdAt;

    public DirectMessageSearchResponse() {
    }

    public DirectMessageSearchResponse(
            Long id,
            Long senderId,
            String senderUsername,
            String content,
            boolean edited,
            boolean deleted,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.edited = edited;
        this.deleted = deleted;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getContent() {
        return content;
    }

    public boolean isEdited() {
        return edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}