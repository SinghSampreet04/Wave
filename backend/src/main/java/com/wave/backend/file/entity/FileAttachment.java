package com.wave.backend.file.entity;

import com.wave.backend.message.entity.Message;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_attachments")
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String storedFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String storagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    public FileAttachment() {
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public Message getMessage() {
        return message;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}