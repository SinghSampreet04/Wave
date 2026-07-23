package com.wave.backend.message.dto;

import java.util.List;

public record MessageHistoryResponse(
        List<MessageResponse> items,
        Long nextCursor,
        boolean hasMore
) {
}
