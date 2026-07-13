package com.wave.backend.channelinvite.dto;

import com.wave.backend.channelinvite.entity.ChannelInvitationStatus;

import java.time.LocalDateTime;

public class ChannelInvitationResponse {

    private Long id;

    private Long channelId;

    private String channelName;

    private Long inviterId;

    private String inviterUsername;

    private Long inviteeId;

    private String inviteeUsername;

    private ChannelInvitationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime respondedAt;

    public ChannelInvitationResponse() {
    }

    public ChannelInvitationResponse(
            Long id,
            Long channelId,
            String channelName,
            Long inviterId,
            String inviterUsername,
            Long inviteeId,
            String inviteeUsername,
            ChannelInvitationStatus status,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime respondedAt
    ) {
        this.id = id;
        this.channelId = channelId;
        this.channelName = channelName;
        this.inviterId = inviterId;
        this.inviterUsername = inviterUsername;
        this.inviteeId = inviteeId;
        this.inviteeUsername = inviteeUsername;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.respondedAt = respondedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public String getInviterUsername() {
        return inviterUsername;
    }

    public Long getInviteeId() {
        return inviteeId;
    }

    public String getInviteeUsername() {
        return inviteeUsername;
    }

    public ChannelInvitationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public void setInviterUsername(String inviterUsername) {
        this.inviterUsername = inviterUsername;
    }

    public void setInviteeId(Long inviteeId) {
        this.inviteeId = inviteeId;
    }

    public void setInviteeUsername(String inviteeUsername) {
        this.inviteeUsername = inviteeUsername;
    }

    public void setStatus(ChannelInvitationStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

}