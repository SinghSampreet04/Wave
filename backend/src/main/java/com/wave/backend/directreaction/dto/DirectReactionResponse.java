package com.wave.backend.directreaction.dto;

public class DirectReactionResponse {

    private Long directMessageId;

    private String emoji;

    private long count;

    private boolean reacted;

    public DirectReactionResponse() {
    }

    public DirectReactionResponse(
            Long directMessageId,
            String emoji,
            long count,
            boolean reacted
    ) {
        this.directMessageId = directMessageId;
        this.emoji = emoji;
        this.count = count;
        this.reacted = reacted;
    }

    public Long getDirectMessageId() {
        return directMessageId;
    }

    public void setDirectMessageId(Long directMessageId) {
        this.directMessageId = directMessageId;
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

    public boolean isReacted() {
        return reacted;
    }

    public void setReacted(boolean reacted) {
        this.reacted = reacted;
    }

}