package com.wave.backend.message.controller;

import com.wave.backend.message.dto.MessageSearchResponse;
import com.wave.backend.message.service.MessageSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages/search")
public class MessageSearchController {

    private final MessageSearchService messageSearchService;

    public MessageSearchController(
            MessageSearchService messageSearchService
    ) {
        this.messageSearchService = messageSearchService;
    }

    @GetMapping
    public List<MessageSearchResponse> searchMessages(
            @RequestParam String q,
            @RequestParam(required = false) Long channelId
    ) {

        if (channelId == null) {
            return messageSearchService.searchMessages(q);
        }

        return messageSearchService.searchMessages(
                channelId,
                q
        );

    }

}