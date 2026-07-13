package com.wave.backend.message.dto;

import java.time.LocalDateTime;

public class ThreadResponse {

    private Long id;

    private Long parentMessageId;

    private Long senderId;

    private String senderUsername;

    private String content;

    private LocalDateTime createdAt;

    public ThreadResponse() {
    }

    public ThreadResponse(
            Long id,
            Long parentMessageId,
            Long senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.parentMessageId = parentMessageId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getParentMessageId() {
        return parentMessageId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentMessageId(Long parentMessageId) {
        this.parentMessageId = parentMessageId;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}