package com.wave.backend.directreaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateDirectReactionRequest {

    @NotNull(message = "Direct message ID is required.")
    private Long directMessageId;

    @NotBlank(message = "Emoji is required.")
    @Size(
            min = 1,
            max = 20,
            message = "Emoji must be between 1 and 20 characters."
    )
    private String emoji;

    public CreateDirectReactionRequest() {
    }

    public CreateDirectReactionRequest(
            Long directMessageId,
            String emoji
    ) {
        this.directMessageId = directMessageId;
        this.emoji = emoji;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

}