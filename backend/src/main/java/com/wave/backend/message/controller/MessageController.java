package com.wave.backend.message.controller;

import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageDeleteResponse;
import com.wave.backend.message.dto.MessageResponse;
import com.wave.backend.message.dto.MessageContextResponse;
import com.wave.backend.message.dto.MessageHistoryResponse;
import com.wave.backend.message.dto.UpdateMessageRequest;
import com.wave.backend.message.service.MessageDeleteService;
import com.wave.backend.message.service.MessageEditService;
import com.wave.backend.message.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;
    private final MessageEditService messageEditService;
    private final MessageDeleteService messageDeleteService;

    public MessageController(
            MessageService messageService,
            MessageEditService messageEditService,
            MessageDeleteService messageDeleteService
    ) {
        this.messageService = messageService;
        this.messageEditService = messageEditService;
        this.messageDeleteService = messageDeleteService;
    }

    @PostMapping
    public MessageResponse sendMessage(
            @Valid @RequestBody CreateMessageRequest request
    ) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/channel/{channelId}")
    public List<MessageResponse> getChannelMessages(
            @PathVariable Long channelId
    ) {
        return messageService.getChannelMessages(channelId);
    }

    @GetMapping("/channel/{channelId}/history")
    public MessageHistoryResponse getChannelHistory(
            @PathVariable Long channelId,
            @RequestParam(required = false) Long before,
            @RequestParam(defaultValue = "50") int size
    ) {
        return messageService.getChannelHistory(channelId, before, size);
    }

    @PatchMapping("/{messageId}")
    public MessageResponse editMessage(
            @PathVariable Long messageId,
            @Valid @RequestBody UpdateMessageRequest request
    ) {
        return messageEditService.editMessage(
                messageId,
                request
        );
    }

    @GetMapping("/{messageId}/context")
    public MessageContextResponse getMessageContext(
            @PathVariable Long messageId
    ) {
        return messageService.getMessageContext(messageId);
    }

    @DeleteMapping("/{messageId}")
    public MessageDeleteResponse deleteMessage(
            @PathVariable Long messageId
    ) {
        return messageDeleteService.deleteMessage(
                messageId
        );
    }
}
