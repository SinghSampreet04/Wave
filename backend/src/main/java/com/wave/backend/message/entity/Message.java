package com.wave.backend.message.entity;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    @OneToMany(mappedBy = "parentMessage")
    private List<Message> replies = new ArrayList<>();

    @Column(nullable = false)
    private boolean edited = false;

    private LocalDateTime editedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public boolean isEdited() {
        return edited;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public void setReplies(List<Message> replies) {
        this.replies = replies;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}