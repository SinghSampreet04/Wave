package com.wave.backend.channel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateChannelRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long workspaceId;

    private boolean isPrivate;

    public CreateChannelRequest() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}