package com.wave.backend.message.dto;

import java.time.LocalDateTime;

public class MessageSearchResponse {

    private Long messageId;

    private Long channelId;
    private String channelName;
    private Long workspaceId;
    private String workspaceName;

    private Long senderId;

    private String senderUsername;

    private String content;

    private LocalDateTime createdAt;
    private boolean hasAttachments;
    private boolean hasReactions;

    public MessageSearchResponse() {
    }

    public MessageSearchResponse(
            Long messageId,
            Long channelId,
            String channelName,
            Long workspaceId,
            String workspaceName,
            Long senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt,
            boolean hasAttachments,
            boolean hasReactions
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.channelName = channelName;
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
        this.hasAttachments = hasAttachments;
        this.hasReactions = hasReactions;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getChannelId() {
        return channelId;
    }
    public String getChannelName() { return channelName; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getWorkspaceName() { return workspaceName; }

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
    public boolean isHasAttachments() { return hasAttachments; }
    public boolean isHasReactions() { return hasReactions; }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public void setWorkspaceName(String workspaceName) { this.workspaceName = workspaceName; }

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
    public void setHasAttachments(boolean hasAttachments) { this.hasAttachments = hasAttachments; }
    public void setHasReactions(boolean hasReactions) { this.hasReactions = hasReactions; }

}
