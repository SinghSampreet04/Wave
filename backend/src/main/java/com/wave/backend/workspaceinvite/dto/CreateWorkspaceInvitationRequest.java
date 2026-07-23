package com.wave.backend.workspaceinvite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class CreateWorkspaceInvitationRequest {

    @NotNull
    private Long workspaceId;

    private Long inviteeId;

    @Email
    private String inviteeEmail;

    public CreateWorkspaceInvitationRequest() {
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public Long getInviteeId() {
        return inviteeId;
    }

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setInviteeId(Long inviteeId) {
        this.inviteeId = inviteeId;
    }

    public void setInviteeEmail(String inviteeEmail) {
        this.inviteeEmail = inviteeEmail;
    }

}
