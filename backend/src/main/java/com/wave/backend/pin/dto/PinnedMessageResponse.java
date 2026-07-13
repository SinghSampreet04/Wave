package com.wave.backend.pin.dto;

import java.time.LocalDateTime;

public class PinnedMessageResponse {

    private Long pinId;

    private Long messageId;

    private Long channelId;

    private Long pinnedByUserId;

    private String pinnedByUsername;

    private String messageContent;

    private LocalDateTime pinnedAt;

    public PinnedMessageResponse() {
    }

    public PinnedMessageResponse(
            Long pinId,
            Long messageId,
            Long channelId,
            Long pinnedByUserId,
            String pinnedByUsername,
            String messageContent,
            LocalDateTime pinnedAt
    ) {
        this.pinId = pinId;
        this.messageId = messageId;
        this.channelId = channelId;
        this.pinnedByUserId = pinnedByUserId;
        this.pinnedByUsername = pinnedByUsername;
        this.messageContent = messageContent;
        this.pinnedAt = pinnedAt;
    }

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getPinnedByUserId() {
        return pinnedByUserId;
    }

    public void setPinnedByUserId(Long pinnedByUserId) {
        this.pinnedByUserId = pinnedByUserId;
    }

    public String getPinnedByUsername() {
        return pinnedByUsername;
    }

    public void setPinnedByUsername(String pinnedByUsername) {
        this.pinnedByUsername = pinnedByUsername;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getPinnedAt() {
        return pinnedAt;
    }

    public void setPinnedAt(LocalDateTime pinnedAt) {
        this.pinnedAt = pinnedAt;
    }

}