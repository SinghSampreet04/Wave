package com.wave.backend.directattachment.dto;

import java.time.LocalDateTime;

public class DirectAttachmentResponse {

    private Long id;

    private Long directMessageId;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    public DirectAttachmentResponse() {
    }

    public DirectAttachmentResponse(
            Long id,
            Long directMessageId,
            String originalFileName,
            String contentType,
            Long fileSize,
            LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.directMessageId = directMessageId;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}