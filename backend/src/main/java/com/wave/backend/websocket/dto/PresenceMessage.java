package com.wave.backend.websocket.dto;

import com.wave.backend.user.entity.PresenceStatus;

public record PresenceMessage(
        Long userId,
        String username,
        PresenceStatus status
) {
}
