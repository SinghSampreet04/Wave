package com.wave.backend.message.dto;

import java.time.LocalDateTime;

public class MessageSearchResponse {

    private Long messageId;

    private Long channelId;

    private Long senderId;

    private String senderUsername;

    private String content;

    private LocalDateTime createdAt;

    public MessageSearchResponse() {
    }

    public MessageSearchResponse(
            Long messageId,
            Long channelId,
            Long senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getChannelId() {
        return channelId;
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

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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