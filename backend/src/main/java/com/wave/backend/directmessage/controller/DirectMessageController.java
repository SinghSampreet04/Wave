package com.wave.backend.directmessage.controller;

import com.wave.backend.directmessage.dto.DirectMessageResponse;
import com.wave.backend.directmessage.dto.SendDirectMessageRequest;
import com.wave.backend.directmessage.service.DirectMessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/direct-messages")
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    public DirectMessageController(
            DirectMessageService directMessageService
    ) {
        this.directMessageService = directMessageService;
    }

    @PostMapping
    public DirectMessageResponse sendMessage(
            @Valid @RequestBody SendDirectMessageRequest request
    ) {

        return directMessageService.sendMessage(request);

    }

    @GetMapping("/conversation/{conversationId}")
    public List<DirectMessageResponse> getConversationMessages(
            @PathVariable Long conversationId
    ) {

        return directMessageService.getConversationMessages(
                conversationId
        );

    }

}