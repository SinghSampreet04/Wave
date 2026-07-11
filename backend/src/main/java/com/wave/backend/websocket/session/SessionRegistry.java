package com.wave.backend.websocket.session;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private final Map<String, SessionInfo> sessions =
            new ConcurrentHashMap<>();

    public void register(SessionInfo sessionInfo) {
        sessions.put(sessionInfo.getSessionId(), sessionInfo);
    }

    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    public SessionInfo getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public Collection<SessionInfo> getSessions() {
        return sessions.values();
    }

    public int getActiveSessionCount() {
        return sessions.size();
    }

}