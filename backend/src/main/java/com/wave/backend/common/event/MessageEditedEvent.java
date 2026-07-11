package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageEditedEvent {

    private final Long messageId;
    private final Long channelId;
    private final String content;
    private final LocalDateTime editedAt;

    public MessageEditedEvent(
            Long messageId,
            Long channelId,
            String content,
            LocalDateTime editedAt
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.content = content;
        this.editedAt = editedAt;
    }

}