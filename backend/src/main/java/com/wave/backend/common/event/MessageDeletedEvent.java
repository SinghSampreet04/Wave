package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDeletedEvent {

    private final Long messageId;

    private final Long channelId;

    private final LocalDateTime deletedAt;

    public MessageDeletedEvent(
            Long messageId,
            Long channelId,
            LocalDateTime deletedAt
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.deletedAt = deletedAt;
    }

}