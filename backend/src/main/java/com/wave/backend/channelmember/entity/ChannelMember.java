package com.wave.backend.channelmember.entity;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "channel_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "channel_id",
                                "user_id"
                        }
                )
        }
)
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    public void onCreate() {

        joinedAt = LocalDateTime.now();

    }

    public ChannelMember() {
    }

    public Long getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

}