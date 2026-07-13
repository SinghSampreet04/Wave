package com.wave.backend.directmessage.dto;

public class DirectMessageDeleteResponse {

    private Long messageId;

    private String message;

    public DirectMessageDeleteResponse() {
    }

    public DirectMessageDeleteResponse(
            Long messageId,
            String message
    ) {
        this.messageId = messageId;
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}