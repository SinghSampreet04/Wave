package com.wave.backend.message.controller;

import com.wave.backend.common.dto.PagedResponse;
import com.wave.backend.message.dto.MessageSearchResponse;
import com.wave.backend.message.service.MessageSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/messages/search")
public class MessageSearchController {

    private final MessageSearchService messageSearchService;

    public MessageSearchController(MessageSearchService messageSearchService) {
        this.messageSearchService = messageSearchService;
    }

    @GetMapping
    public PagedResponse<MessageSearchResponse> searchMessages(
            @RequestParam String q,
            @RequestParam(required = false) Long workspaceId,
            @RequestParam(required = false) Long channelId,
            @RequestParam(required = false) Long senderId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,
            @RequestParam(required = false) Boolean hasAttachment,
            @RequestParam(required = false) Boolean hasReaction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return PagedResponse.from(
                messageSearchService.searchMessages(
                        q,
                        workspaceId,
                        channelId,
                        senderId,
                        from,
                        to,
                        hasAttachment,
                        hasReaction,
                        page,
                        size
                )
        );
    }
}
