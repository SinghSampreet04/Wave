package com.wave.backend.directmessage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendDirectMessageRequest {

    @NotNull(message = "Conversation id is required.")
    private Long conversationId;

    @NotBlank(message = "Message content is required.")
    private String content;

    public SendDirectMessageRequest() {
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}