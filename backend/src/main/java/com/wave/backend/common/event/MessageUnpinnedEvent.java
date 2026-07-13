package com.wave.backend.common.event;

import lombok.Getter;

@Getter
public class MessageUnpinnedEvent {

    private final Long messageId;

    private final Long channelId;

    public MessageUnpinnedEvent(
            Long messageId,
            Long channelId
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
    }

}