package com.wave.backend.directmessage.dto;

public class DirectTypingMessage {

    private Long conversationId;

    private boolean typing;

    public DirectTypingMessage() {
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

}