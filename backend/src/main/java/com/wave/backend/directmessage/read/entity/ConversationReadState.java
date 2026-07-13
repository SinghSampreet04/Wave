package com.wave.backend.directmessage.read.entity;

import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "conversation_read_states",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "conversation_id",
                                "user_id"
                        }
                )
        }
)
public class ConversationReadState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "conversation_id",
            nullable = false
    )
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id")
    private DirectMessage lastReadMessage;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public ConversationReadState() {
    }

    public Long getId() {
        return id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public User getUser() {
        return user;
    }

    public DirectMessage getLastReadMessage() {
        return lastReadMessage;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLastReadMessage(
            DirectMessage lastReadMessage
    ) {
        this.lastReadMessage = lastReadMessage;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}