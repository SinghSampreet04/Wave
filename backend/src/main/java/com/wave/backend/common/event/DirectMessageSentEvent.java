package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DirectMessageSentEvent {

    private final Long messageId;

    private final Long conversationId;

    private final Long senderId;

    private final String senderUsername;

    private final String content;

    private final LocalDateTime createdAt;

    public DirectMessageSentEvent(
            Long messageId,
            Long conversationId,
            Long senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt
    ) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

}