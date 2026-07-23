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
import com.wave.backend.websocket.service.WebSocketAccessService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final SessionRegistry sessionRegistry;
    private final WebSocketAccessService webSocketAccessService;

    public ChatWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            MessageService messageService,
            SessionRegistry sessionRegistry,
            WebSocketAccessService webSocketAccessService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.sessionRegistry = sessionRegistry;
        this.webSocketAccessService = webSocketAccessService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(
            ChatMessage chatMessage,
            Principal principal
    ) {
        if (principal == null) {
            return;
        }

        CreateMessageRequest request = new CreateMessageRequest();
        request.setChannelId(chatMessage.getChannelId());
        request.setContent(chatMessage.getContent());

        messageService.sendMessage(
                        request,
                        principal.getName()
                );
    }

    @MessageMapping("/chat.typing")
    public void typing(
            TypingMessage message,
            @Header("simpSessionId") String sessionId,
            Principal principal
    ) {

        if (principal == null) {
            return;
        }

        SessionInfo session = sessionRegistry.getSession(sessionId);

        if (session == null) {
            return;
        }

        webSocketAccessService.validateChannel(
                message.getChannelId(),
                webSocketAccessService.getUser(principal.getName())
        );

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
