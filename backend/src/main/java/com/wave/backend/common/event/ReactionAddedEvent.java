package com.wave.backend.common.event;

import lombok.Getter;

@Getter
public class ReactionAddedEvent {

    private final Long messageId;

    private final Long channelId;

    private final String emoji;

    private final long count;
    private final Long actorUserId;
    private final boolean reacted;

    public ReactionAddedEvent(
            Long messageId,
            Long channelId,
            String emoji,
            long count,
            Long actorUserId,
            boolean reacted
    ) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.emoji = emoji;
        this.count = count;
        this.actorUserId = actorUserId;
        this.reacted = reacted;
    }

}
