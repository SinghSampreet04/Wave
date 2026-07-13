package com.wave.backend.common.event;

public class MentionCreatedEvent {

    private final Long mentionId;
    private final Long mentionedUserId;
    private final Long mentionedByUserId;
    private final String mentionedByUsername;
    private final Long messageId;
    private final Long directMessageId;

    public MentionCreatedEvent(
            Long mentionId,
            Long mentionedUserId,
            Long mentionedByUserId,
            String mentionedByUsername,
            Long messageId,
            Long directMessageId
    ) {
        this.mentionId = mentionId;
        this.mentionedUserId = mentionedUserId;
        this.mentionedByUserId = mentionedByUserId;
        this.mentionedByUsername = mentionedByUsername;
        this.messageId = messageId;
        this.directMessageId = directMessageId;
    }

    public Long getMentionId() {
        return mentionId;
    }

    public Long getMentionedUserId() {
        return mentionedUserId;
    }

    public Long getMentionedByUserId() {
        return mentionedByUserId;
    }

    public String getMentionedByUsername() {
        return mentionedByUsername;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

}