package com.wave.backend.websocket.listener;

import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.websocket.service.PresenceService;
import com.wave.backend.websocket.session.SessionInfo;
import com.wave.backend.websocket.session.SessionRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class PresenceEventListener {

    private final SessionRegistry sessionRegistry;
    private final PresenceService presenceService;
    private final UserRepository userRepository;

    public PresenceEventListener(
            SessionRegistry sessionRegistry,
            PresenceService presenceService,
            UserRepository userRepository
    ) {
        this.sessionRegistry = sessionRegistry;
        this.presenceService = presenceService;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleWebSocketConnectListener(
            SessionConnectEvent event
    ) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null) {
            return;
        }

        String username = (String) sessionAttributes.get("username");

        if (username == null) {
            return;
        }

        User user = userRepository.findByEmail(username)
                .orElse(null);

        if (user == null) {
            return;
        }

        SessionInfo session = new SessionInfo(
                accessor.getSessionId(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                LocalDateTime.now()
        );

        sessionRegistry.register(session);

        presenceService.userConnected(user, accessor.getSessionId());

    }

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {

        String sessionId = event.getSessionId();

        SessionInfo session =
                sessionRegistry.getSession(sessionId);

        if (session == null) {
            return;
        }

        User user = userRepository.findById(session.getUserId()).orElse(null);
        if (user != null) {
            presenceService.userDisconnected(user, sessionId);
        }

        sessionRegistry.unregister(sessionId);

    }

}
