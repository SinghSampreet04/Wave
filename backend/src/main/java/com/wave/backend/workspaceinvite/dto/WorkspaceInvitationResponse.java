package com.wave.backend.workspaceinvite.dto;

import com.wave.backend.workspaceinvite.entity.WorkspaceInvitationStatus;

import java.time.LocalDateTime;

public class WorkspaceInvitationResponse {

    private Long id;

    private Long workspaceId;

    private String workspaceName;

    private Long inviterId;

    private String inviterUsername;

    private Long inviteeId;

    private String inviteeUsername;

    private WorkspaceInvitationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime respondedAt;

    public WorkspaceInvitationResponse() {
    }

    public WorkspaceInvitationResponse(
            Long id,
            Long workspaceId,
            String workspaceName,
            Long inviterId,
            String inviterUsername,
            Long inviteeId,
            String inviteeUsername,
            WorkspaceInvitationStatus status,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime respondedAt
    ) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
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

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
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

    public WorkspaceInvitationStatus getStatus() {
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

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
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

    public void setStatus(WorkspaceInvitationStatus status) {
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