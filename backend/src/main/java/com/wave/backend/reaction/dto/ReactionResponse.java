package com.wave.backend.reaction.dto;

public class ReactionResponse {

    private Long messageId;

    private String emoji;

    private long count;

    private boolean reactedByCurrentUser;

    public ReactionResponse() {
    }

    public ReactionResponse(
            Long messageId,
            String emoji,
            long count,
            boolean reactedByCurrentUser
    ) {
        this.messageId = messageId;
        this.emoji = emoji;
        this.count = count;
        this.reactedByCurrentUser = reactedByCurrentUser;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isReactedByCurrentUser() {
        return reactedByCurrentUser;
    }

    public void setReactedByCurrentUser(boolean reactedByCurrentUser) {
        this.reactedByCurrentUser = reactedByCurrentUser;
    }

}