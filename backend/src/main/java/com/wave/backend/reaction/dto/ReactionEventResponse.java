package com.wave.backend.reaction.dto;

public record ReactionEventResponse(
        Long messageId,
        String emoji,
        long count,
        Long actorUserId,
        boolean reacted
) {
}
