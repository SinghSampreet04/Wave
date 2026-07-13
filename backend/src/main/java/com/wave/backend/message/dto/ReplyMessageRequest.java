package com.wave.backend.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReplyMessageRequest {

    @NotNull(message = "Parent message ID is required.")
    private Long parentMessageId;

    @NotBlank(message = "Reply content is required.")
    private String content;

    public ReplyMessageRequest() {
    }

    public Long getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(Long parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}