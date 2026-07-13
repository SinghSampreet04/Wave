package com.wave.backend.directmessage.dto;

import java.time.LocalDateTime;

public class ConversationResponse {

    private Long conversationId;

    private Long otherUserId;

    private String otherUsername;

    private String otherFirstName;

    private String otherLastName;

    private String otherAvatar;

    private String lastMessage;

    private LocalDateTime lastMessageAt;

    private LocalDateTime createdAt;

    public ConversationResponse() {
    }

    public ConversationResponse(
            Long conversationId,
            Long otherUserId,
            String otherUsername,
            String otherFirstName,
            String otherLastName,
            String otherAvatar,
            String lastMessage,
            LocalDateTime lastMessageAt,
            LocalDateTime createdAt
    ) {
        this.conversationId = conversationId;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
        this.otherFirstName = otherFirstName;
        this.otherLastName = otherLastName;
        this.otherAvatar = otherAvatar;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.createdAt = createdAt;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getOtherFirstName() {
        return otherFirstName;
    }

    public void setOtherFirstName(String otherFirstName) {
        this.otherFirstName = otherFirstName;
    }

    public String getOtherLastName() {
        return otherLastName;
    }

    public void setOtherLastName(String otherLastName) {
        this.otherLastName = otherLastName;
    }

    public String getOtherAvatar() {
        return otherAvatar;
    }

    public void setOtherAvatar(String otherAvatar) {
        this.otherAvatar = otherAvatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}