package com.wave.backend.websocket.controller;

import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.service.MessageService;
import com.wave.backend.websocket.dto.ChatMessage;
import com.wave.backend.websocket.dto.ChatMessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            MessageService messageService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage) {

        CreateMessageRequest request = new CreateMessageRequest();
        request.setChannelId(chatMessage.getChannelId());
        request.setContent(chatMessage.getContent());

        Message savedMessage = messageService.sendMessage(request);

        ChatMessageResponse response =
                new ChatMessageResponse(
                        savedMessage.getId(),
                        savedMessage.getChannel().getId(),
                        savedMessage.getSender().getId(),
                        savedMessage.getSender().getUsername(),
                        savedMessage.getContent(),
                        savedMessage.getCreatedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/channel/" + savedMessage.getChannel().getId(),
                response
        );
    }
}