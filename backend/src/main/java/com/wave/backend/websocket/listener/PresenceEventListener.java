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

        String username =
                (String) accessor.getSessionAttributes().get("username");

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

        presenceService.userConnected(user.getEmail());

        System.out.println("--------------------------------");
        System.out.println("User Connected");
        System.out.println("Session : " + accessor.getSessionId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Online Sessions: "
                + sessionRegistry.getActiveSessionCount());
        System.out.println("--------------------------------");

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

        presenceService.userDisconnected(session.getEmail());

        sessionRegistry.unregister(sessionId);

        System.out.println("--------------------------------");
        System.out.println("User Disconnected");
        System.out.println("Session : " + sessionId);
        System.out.println("Username: " + session.getUsername());
        System.out.println("Online Sessions: "
                + sessionRegistry.getActiveSessionCount());
        System.out.println("--------------------------------");

    }

}