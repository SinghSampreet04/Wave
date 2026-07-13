package com.wave.backend.message.controller;

import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageDeleteResponse;
import com.wave.backend.message.dto.MessageEditResponse;
import com.wave.backend.message.dto.UpdateMessageRequest;
import com.wave.backend.message.entity.Message;
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
    public Message sendMessage(
            @Valid @RequestBody CreateMessageRequest request
    ) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/channel/{channelId}")
    public List<Message> getChannelMessages(
            @PathVariable Long channelId
    ) {
        return messageService.getChannelMessages(channelId);
    }

    @PatchMapping("/{messageId}")
    public MessageEditResponse editMessage(
            @PathVariable Long messageId,
            @Valid @RequestBody UpdateMessageRequest request
    ) {
        return messageEditService.editMessage(
                messageId,
                request
        );
    }

    @DeleteMapping("/{messageId}")
    public MessageDeleteResponse deleteMessage(
            @PathVariable Long messageId
    ) {
        return messageDeleteService.deleteMessage(messageId);
    }

}