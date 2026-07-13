package com.wave.backend.channelinvite.entity;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "channel_invitations",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "channel_id",
                                "invitee_id",
                                "status"
                        }
                )
        }
)
public class ChannelInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invitee_id")
    private User invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelInvitationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime respondedAt;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusDays(7);
        status = ChannelInvitationStatus.PENDING;

    }

    public ChannelInvitation() {
    }

    public Long getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getInviter() {
        return inviter;
    }

    public User getInvitee() {
        return invitee;
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

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
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