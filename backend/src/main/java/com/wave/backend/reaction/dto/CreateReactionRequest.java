package com.wave.backend.reaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReactionRequest {

    @NotNull(message = "Message ID is required.")
    private Long messageId;

    @NotBlank(message = "Emoji is required.")
    private String emoji;

    public CreateReactionRequest() {
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

}