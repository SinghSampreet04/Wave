package com.wave.backend.workspaceinvite.entity;

import com.wave.backend.user.entity.User;
import com.wave.backend.workspace.entity.Workspace;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "workspace_invitations",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "workspace_id",
                                "invitee_id"
                        }
                )
        }
)
public class WorkspaceInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invitee_id")
    private User invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkspaceInvitationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime respondedAt;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();

        expiresAt = createdAt.plusDays(7);

        status = WorkspaceInvitationStatus.PENDING;

    }

    public WorkspaceInvitation() {
    }

    public Long getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public User getInviter() {
        return inviter;
    }

    public User getInvitee() {
        return invitee;
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

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public void setStatus(
            WorkspaceInvitationStatus status
    ) {
        this.status = status;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(
            LocalDateTime expiresAt
    ) {
        this.expiresAt = expiresAt;
    }

    public void setRespondedAt(
            LocalDateTime respondedAt
    ) {
        this.respondedAt = respondedAt;
    }

}