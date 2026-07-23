package com.wave.backend.message.dto;

public record MessageContextResponse(
        Long messageId,
        Long channelId,
        Long workspaceId
) {
}
