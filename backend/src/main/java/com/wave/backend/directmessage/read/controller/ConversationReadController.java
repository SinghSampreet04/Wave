package com.wave.backend.directmessage.read.controller;

import com.wave.backend.directmessage.read.service.ConversationReadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/direct-messages")
public class ConversationReadController {

    private final ConversationReadService conversationReadService;

    public ConversationReadController(
            ConversationReadService conversationReadService
    ) {
        this.conversationReadService = conversationReadService;
    }

    @PostMapping("/{conversationId}/read/{messageId}")
    public void markConversationAsRead(
            @PathVariable Long conversationId,
            @PathVariable Long messageId
    ) {

        conversationReadService.markConversationAsRead(
                conversationId,
                messageId
        );

    }

    @GetMapping("/{conversationId}/unread-count")
    public long getUnreadCount(
            @PathVariable Long conversationId
    ) {

        return conversationReadService.getUnreadCount(
                conversationId
        );

    }

}