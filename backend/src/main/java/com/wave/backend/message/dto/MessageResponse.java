package com.wave.backend.message.dto;

import com.wave.backend.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageResponse {

    private Long id;

    private Long senderId;

    private String senderUsername;

    private Long channelId;

    private String content;

    private boolean edited;

    private boolean deleted;

    private List<ReactionResponse> reactions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MessageResponse() {
    }

    public MessageResponse(
            Long id,
            Long senderId,
            String senderUsername,
            Long channelId,
            String content,
            boolean edited,
            boolean deleted,
            List<ReactionResponse> reactions,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.channelId = channelId;
        this.content = content;
        this.edited = edited;
        this.deleted = deleted;
        this.reactions = reactions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Long getChannelId() {
        return channelId;
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

    public List<ReactionResponse> getReactions() {
        return reactions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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

    public void setReactions(List<ReactionResponse> reactions) {
        this.reactions = reactions;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}