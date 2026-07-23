package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DirectMessageEditedEvent {
    private final Long messageId;
    private final Long conversationId;
    private final String content;
    private final LocalDateTime updatedAt;

    public DirectMessageEditedEvent(
            Long messageId,
            Long conversationId,
            String content,
            LocalDateTime updatedAt
    ) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
