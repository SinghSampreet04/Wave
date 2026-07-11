package com.wave.backend.websocket.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SessionInfo {

    private String sessionId;

    private Long userId;

    private String username;

    private String email;

    private LocalDateTime connectedAt;

}