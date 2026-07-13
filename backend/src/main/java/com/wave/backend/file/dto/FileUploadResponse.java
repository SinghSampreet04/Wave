package com.wave.backend.file.dto;

public class FileUploadResponse {

    private Long id;

    private String originalFilename;

    private String storedFilename;

    private String contentType;

    private Long fileSize;

    public FileUploadResponse() {
    }

    public FileUploadResponse(
            Long id,
            String originalFilename,
            String storedFilename,
            String contentType,
            Long fileSize
    ) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
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

}