package com.wave.backend.file.dto;

public class FileDownloadResponse {

    private Long id;

    private String originalFilename;

    private String contentType;

    private Long fileSize;

    public FileDownloadResponse() {
    }

    public FileDownloadResponse(
            Long id,
            String originalFilename,
            String contentType,
            Long fileSize
    ) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
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

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

}