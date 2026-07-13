package com.wave.backend.directmessage.dto;

import java.time.LocalDateTime;

public class DirectMessageResponse {

    private Long messageId;

    private Long conversationId;

    private Long senderId;

    private String senderUsername;

    private String content;

    private boolean edited;

    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public DirectMessageResponse() {
    }

    public DirectMessageResponse(
            Long messageId,
            Long conversationId,
            Long senderId,
            String senderUsername,
            String content,
            boolean edited,
            boolean deleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.edited = edited;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}