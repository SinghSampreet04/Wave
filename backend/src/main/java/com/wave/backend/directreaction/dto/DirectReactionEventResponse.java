package com.wave.backend.directreaction.dto;

public record DirectReactionEventResponse(
        Long directMessageId,
        String emoji,
        long count,
        Long actorUserId,
        boolean reacted
) {
}
