package com.wave.backend.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ThreadReplyEvent {

    private final Long replyId;

    private final Long parentMessageId;

    private final Long channelId;

    private final Long senderId;

    private final String senderUsername;

    private final String content;

    private final LocalDateTime createdAt;

    public ThreadReplyEvent(
            Long replyId,
            Long parentMessageId,
            Long channelId,
            Long senderId,
            String senderUsername,
            String content,
            LocalDateTime createdAt
    ) {
        this.replyId = replyId;
        this.parentMessageId = parentMessageId;
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

}