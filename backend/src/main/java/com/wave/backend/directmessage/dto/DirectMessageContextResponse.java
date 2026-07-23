package com.wave.backend.directmessage.dto;

public record DirectMessageContextResponse(
        Long messageId,
        Long conversationId
) {
}
