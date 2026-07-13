package com.wave.backend.channelmember.dto;

import java.time.LocalDateTime;

public class ChannelMemberResponse {

    private Long id;

    private Long channelId;

    private String channelName;

    private Long userId;

    private String username;

    private LocalDateTime joinedAt;

    public ChannelMemberResponse() {
    }

    public ChannelMemberResponse(
            Long id,
            Long channelId,
            String channelName,
            Long userId,
            String username,
            LocalDateTime joinedAt
    ) {
        this.id = id;
        this.channelId = channelId;
        this.channelName = channelName;
        this.userId = userId;
        this.username = username;
        this.joinedAt = joinedAt;
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

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

}