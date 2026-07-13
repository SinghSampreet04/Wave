package com.wave.backend.channelinvite.dto;

import jakarta.validation.constraints.NotNull;

public class CreateChannelInvitationRequest {

    @NotNull
    private Long channelId;

    @NotNull
    private Long inviteeId;

    public CreateChannelInvitationRequest() {
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(Long inviteeId) {
        this.inviteeId = inviteeId;
    }

}