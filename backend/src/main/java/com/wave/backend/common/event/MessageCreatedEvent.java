package com.wave.backend.common.event;

import com.wave.backend.message.dto.MessageResponse;

public record MessageCreatedEvent(MessageResponse message) {
}
