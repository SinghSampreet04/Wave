package com.wave.backend.pin.entity;

import com.wave.backend.message.entity.Message;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "pinned_messages",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "message_id"
                        }
                )
        }
)
public class PinnedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "message_id",
            nullable = false
    )
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "pinned_by",
            nullable = false
    )
    private User pinnedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime pinnedAt;

    @PrePersist
    public void onCreate() {
        pinnedAt = LocalDateTime.now();
    }

    public PinnedMessage() {
    }

    public Long getId() {
        return id;
    }

    public Message getMessage() {
        return message;
    }

    public User getPinnedBy() {
        return pinnedBy;
    }

    public LocalDateTime getPinnedAt() {
        return pinnedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setPinnedBy(User pinnedBy) {
        this.pinnedBy = pinnedBy;
    }

    public void setPinnedAt(LocalDateTime pinnedAt) {
        this.pinnedAt = pinnedAt;
    }

}