package com.wave.backend.directmessage.dto;

public class DirectChatMessage {

    private Long conversationId;

    private String content;

    public DirectChatMessage() {
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}