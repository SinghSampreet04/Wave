package com.wave.backend.message.controller;

import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponse sendMessage(
            @Valid @RequestBody CreateMessageRequest request
    ) {

        Message message = messageService.sendMessage(request);

        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getChannel().getId(),
                message.getCreatedAt()
        );
    }

    @GetMapping("/channel/{channelId}")
    public List<MessageResponse> getChannelMessages(
            @PathVariable Long channelId
    ) {

        return messageService.getChannelMessages(channelId)
                .stream()
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getContent(),
                        message.getSender().getId(),
                        message.getSender().getUsername(),
                        message.getChannel().getId(),
                        message.getCreatedAt()
                ))
                .toList();
    }
}