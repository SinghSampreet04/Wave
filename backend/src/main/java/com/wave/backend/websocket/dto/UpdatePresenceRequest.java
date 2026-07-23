package com.wave.backend.websocket.dto;

import com.wave.backend.user.entity.PresenceStatus;
import jakarta.validation.constraints.NotNull;

public class UpdatePresenceRequest {

    @NotNull
    private PresenceStatus status;

    public PresenceStatus getStatus() {
        return status;
    }

    public void setStatus(PresenceStatus status) {
        this.status = status;
    }
}
