package com.wave.backend.directmessage.controller;

import com.wave.backend.directmessage.dto.DirectChatMessage;
import com.wave.backend.directmessage.dto.DirectMessageResponse;
import com.wave.backend.directmessage.dto.DirectTypingMessage;
import com.wave.backend.directmessage.dto.SendDirectMessageRequest;
import com.wave.backend.directmessage.service.DirectMessageService;
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
public class DirectMessageWebSocketController {

    private final DirectMessageService directMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistry sessionRegistry;
    private final WebSocketAccessService webSocketAccessService;

    public DirectMessageWebSocketController(
            DirectMessageService directMessageService,
            SimpMessagingTemplate messagingTemplate,
            SessionRegistry sessionRegistry,
            WebSocketAccessService webSocketAccessService
    ) {
        this.directMessageService = directMessageService;
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistry = sessionRegistry;
        this.webSocketAccessService = webSocketAccessService;
    }

    @MessageMapping("/direct.send")
    public void sendDirectMessage(
            DirectChatMessage chatMessage,
            Principal principal
    ) {

        if (principal == null) {
            return;
        }

        SendDirectMessageRequest request =
                new SendDirectMessageRequest();

        request.setConversationId(
                chatMessage.getConversationId()
        );

        request.setContent(
                chatMessage.getContent()
        );

        DirectMessageResponse response =
                directMessageService.sendMessage(
                        request,
                        principal.getName()
                );

    }

    @MessageMapping("/direct.typing")
    public void typing(
            DirectTypingMessage message,
            @Header("simpSessionId") String sessionId,
            Principal principal
    ) {

        if (principal == null) {
            return;
        }

        SessionInfo session =
                sessionRegistry.getSession(sessionId);

        if (session == null) {
            return;
        }

        webSocketAccessService.validateConversation(
                message.getConversationId(),
                webSocketAccessService.getUser(principal.getName())
        );

        TypingResponse response =
                new TypingResponse(
                        session.getUserId(),
                        session.getUsername(),
                        message.isTyping()
                );

        messagingTemplate.convertAndSend(
                "/topic/conversations/"
                        + message.getConversationId()
                        + "/typing",
                response
        );

    }

}
