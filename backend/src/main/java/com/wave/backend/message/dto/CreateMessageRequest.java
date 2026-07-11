package com.wave.backend.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMessageRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long channelId;

    public CreateMessageRequest() {
    }

    public String getContent() {
        return content;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}