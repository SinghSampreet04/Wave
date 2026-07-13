package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessagePinnedEvent {

    private final Long messageId;

    private final Long channelId;

    private final Long pinnedByUserId;

    private final String pinnedByUsername;

    private final LocalDateTime pinnedAt;

    public MessagePinnedEvent(
            Long messageId,
            Long channelId,
            Long pinnedByUserId,
            String pinnedByUsername,
            LocalDateTime pinnedAt
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.pinnedByUserId = pinnedByUserId;
        this.pinnedByUsername = pinnedByUsername;
        this.pinnedAt = pinnedAt;
    }

}