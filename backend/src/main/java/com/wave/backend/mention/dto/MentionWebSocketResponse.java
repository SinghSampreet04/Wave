package com.wave.backend.mention.dto;

import java.time.LocalDateTime;

public class MentionWebSocketResponse {

    private Long mentionId;

    private Long mentionedById;

    private String mentionedByUsername;

    private Long messageId;

    private Long directMessageId;

    private LocalDateTime createdAt;

    public MentionWebSocketResponse() {
    }

    public MentionWebSocketResponse(
            Long mentionId,
            Long mentionedById,
            String mentionedByUsername,
            Long messageId,
            Long directMessageId,
            LocalDateTime createdAt
    ) {
        this.mentionId = mentionId;
        this.mentionedById = mentionedById;
        this.mentionedByUsername = mentionedByUsername;
        this.messageId = messageId;
        this.directMessageId = directMessageId;
        this.createdAt = createdAt;
    }

    public Long getMentionId() {
        return mentionId;
    }

    public void setMentionId(Long mentionId) {
        this.mentionId = mentionId;
    }

    public Long getMentionedById() {
        return mentionedById;
    }

    public void setMentionedById(Long mentionedById) {
        this.mentionedById = mentionedById;
    }

    public String getMentionedByUsername() {
        return mentionedByUsername;
    }

    public void setMentionedByUsername(String mentionedByUsername) {
        this.mentionedByUsername = mentionedByUsername;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}