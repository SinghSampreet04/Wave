package com.wave.backend.message.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;
    private String content;
    private Long senderId;
    private String senderUsername;
    private Long channelId;
    private LocalDateTime createdAt;

    public MessageResponse() {
    }

    public MessageResponse(
            Long id,
            String content,
            Long senderId,
            String senderUsername,
            Long channelId,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.channelId = channelId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Long getChannelId() {
        return channelId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}