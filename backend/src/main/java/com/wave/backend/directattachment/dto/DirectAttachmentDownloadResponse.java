package com.wave.backend.directattachment.dto;

public class DirectAttachmentDownloadResponse {

    private String originalFileName;

    private String contentType;

    private byte[] data;

    public DirectAttachmentDownloadResponse() {
    }

    public DirectAttachmentDownloadResponse(
            String originalFileName,
            String contentType,
            byte[] data
    ) {
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.data = data;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}