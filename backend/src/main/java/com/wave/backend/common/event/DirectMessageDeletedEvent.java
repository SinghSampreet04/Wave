package com.wave.backend.common.event;

import lombok.Getter;

@Getter
public class DirectMessageDeletedEvent {
    private final Long messageId;
    private final Long conversationId;

    public DirectMessageDeletedEvent(
            Long messageId,
            Long conversationId
    ) {
        this.messageId = messageId;
        this.conversationId = conversationId;
    }
}
