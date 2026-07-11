package com.wave.backend.channel.dto;

public class ChannelResponse {

    private Long id;
    private String name;
    private String description;
    private boolean isPrivate;
    private Long workspaceId;

    public ChannelResponse() {
    }

    public ChannelResponse(
            Long id,
            String name,
            String description,
            boolean isPrivate,
            Long workspaceId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.workspaceId = workspaceId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}