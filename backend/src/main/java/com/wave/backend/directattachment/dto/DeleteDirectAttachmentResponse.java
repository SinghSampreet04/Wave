package com.wave.backend.directattachment.dto;

public class DeleteDirectAttachmentResponse {

    private Long attachmentId;

    private String message;

    public DeleteDirectAttachmentResponse() {
    }

    public DeleteDirectAttachmentResponse(
            Long attachmentId,
            String message
    ) {
        this.attachmentId = attachmentId;
        this.message = message;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public String getMessage() {
        return message;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}