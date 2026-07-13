package com.wave.backend.directmessage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateDirectMessageRequest {

    @NotBlank(message = "Message content is required.")
    @Size(
            min = 1,
            max = 4000,
            message = "Message must be between 1 and 4000 characters."
    )
    private String content;

    public UpdateDirectMessageRequest() {
    }

    public UpdateDirectMessageRequest(
            String content
    ) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(
            String content
    ) {
        this.content = content;
    }

}