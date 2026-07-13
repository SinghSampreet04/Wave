package com.wave.backend.mention.dto;

import java.time.LocalDateTime;

public class MentionResponse {

    private Long id;

    private Long mentionedById;

    private String mentionedByUsername;

    private Long messageId;

    private Long directMessageId;

    private boolean read;

    private LocalDateTime createdAt;

    public MentionResponse() {
    }

    public MentionResponse(
            Long id,
            Long mentionedById,
            String mentionedByUsername,
            Long messageId,
            Long directMessageId,
            boolean read,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.mentionedById = mentionedById;
        this.mentionedByUsername = mentionedByUsername;
        this.messageId = messageId;
        this.directMessageId = directMessageId;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMentionedById() {
        return mentionedById;
    }

    public String getMentionedByUsername() {
        return mentionedByUsername;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getDirectMessageId() {
        return directMessageId;
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

    public void setMentionedById(Long mentionedById) {
        this.mentionedById = mentionedById;
    }

    public void setMentionedByUsername(String mentionedByUsername) {
        this.mentionedByUsername = mentionedByUsername;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}