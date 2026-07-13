package com.wave.backend.workspaceinvite.dto;

import jakarta.validation.constraints.NotNull;

public class CreateWorkspaceInvitationRequest {

    @NotNull
    private Long workspaceId;

    @NotNull
    private Long inviteeId;

    public CreateWorkspaceInvitationRequest() {
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public Long getInviteeId() {
        return inviteeId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setInviteeId(Long inviteeId) {
        this.inviteeId = inviteeId;
    }

}