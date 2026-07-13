package com.wave.backend.directreaction.entity;

import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "direct_reactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "direct_message_id",
                                "user_id",
                                "emoji"
                        }
                )
        }
)
public class DirectReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "direct_message_id")
    private DirectMessage directMessage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 20)
    private String emoji;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public DirectReaction() {
    }

    public Long getId() {
        return id;
    }

    public DirectMessage getDirectMessage() {
        return directMessage;
    }

    public User getUser() {
        return user;
    }

    public String getEmoji() {
        return emoji;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDirectMessage(DirectMessage directMessage) {
        this.directMessage = directMessage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}