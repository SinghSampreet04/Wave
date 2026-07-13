package com.wave.backend.directmessage.dto;

import jakarta.validation.constraints.NotNull;

public class CreateConversationRequest {

    @NotNull(message = "Recipient user id is required.")
    private Long recipientUserId;

    public CreateConversationRequest() {
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

}