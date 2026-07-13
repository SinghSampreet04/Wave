package com.wave.backend.directmessage.dto;

import java.time.LocalDateTime;

public class DirectMessageEditResponse {

    private Long messageId;

    private String content;

    private boolean edited;

    private LocalDateTime updatedAt;

    public DirectMessageEditResponse() {
    }

    public DirectMessageEditResponse(
            Long messageId,
            String content,
            boolean edited,
            LocalDateTime updatedAt
    ) {
        this.messageId = messageId;
        this.content = content;
        this.edited = edited;
        this.updatedAt = updatedAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getContent() {
        return content;
    }

    public boolean isEdited() {
        return edited;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}