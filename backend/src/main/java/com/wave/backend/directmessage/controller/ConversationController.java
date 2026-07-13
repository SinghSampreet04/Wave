package com.wave.backend.directmessage.controller;

import com.wave.backend.directmessage.dto.ConversationResponse;
import com.wave.backend.directmessage.dto.CreateConversationRequest;
import com.wave.backend.directmessage.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(
            ConversationService conversationService
    ) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ConversationResponse createConversation(
            @Valid @RequestBody CreateConversationRequest request
    ) {

        return conversationService.createConversation(request);

    }

    @GetMapping
    public List<ConversationResponse> getMyConversations() {

        return conversationService.getMyConversations();

    }

}