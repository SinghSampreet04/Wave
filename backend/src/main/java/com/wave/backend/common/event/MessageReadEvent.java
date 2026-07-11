package com.wave.backend.common.event;

import lombok.Getter;

@Getter
public class MessageReadEvent {

    private final Long messageId;
    private final Long channelId;
    private final Long userId;
    private final String username;

    public MessageReadEvent(
            Long messageId,
            Long channelId,
            Long userId,
            String username
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.userId = userId;
        this.username = username;
    }

}