package com.wave.backend.websocket.controller;

import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageResponse;
import com.wave.backend.message.service.MessageService;
import com.wave.backend.websocket.dto.ChatMessage;
import com.wave.backend.websocket.dto.ChatMessageResponse;
import com.wave.backend.websocket.dto.TypingMessage;
import com.wave.backend.websocket.dto.TypingResponse;
import com.wave.backend.websocket.session.SessionInfo;
import com.wave.backend.websocket.session.SessionRegistry;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final SessionRegistry sessionRegistry;

    public ChatWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            MessageService messageService,
            SessionRegistry sessionRegistry
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.sessionRegistry = sessionRegistry;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage) {

        CreateMessageRequest request = new CreateMessageRequest();
        request.setChannelId(chatMessage.getChannelId());
        request.setContent(chatMessage.getContent());

        MessageResponse savedMessage =
                messageService.sendMessage(request);

        ChatMessageResponse response =
                new ChatMessageResponse(
                        savedMessage.getId(),
                        savedMessage.getChannelId(),
                        savedMessage.getSenderId(),
                        savedMessage.getSenderUsername(),
                        savedMessage.getContent(),
                        savedMessage.getCreatedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/channels/" + savedMessage.getChannelId(),
                response
        );
    }

    @MessageMapping("/chat.typing")
    public void typing(
            TypingMessage message,
            @Header("simpSessionId") String sessionId
    ) {

        SessionInfo session = sessionRegistry.getSession(sessionId);

        if (session == null) {
            return;
        }

        TypingResponse response = new TypingResponse(
                session.getUserId(),
                session.getUsername(),
                message.isTyping()
        );

        messagingTemplate.convertAndSend(
                "/topic/channels/" + message.getChannelId() + "/typing",
                response
        );
    }

}