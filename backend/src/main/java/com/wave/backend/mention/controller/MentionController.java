package com.wave.backend.mention.controller;

import com.wave.backend.mention.dto.MentionResponse;
import com.wave.backend.mention.service.MentionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mentions")
public class MentionController {

    private final MentionService mentionService;

    public MentionController(
            MentionService mentionService
    ) {
        this.mentionService = mentionService;
    }

    @GetMapping
    public List<MentionResponse> getMyMentions() {

        return mentionService.getMyMentions();

    }

    @GetMapping("/unread-count")
    public long getUnreadMentionCount() {

        return mentionService.getUnreadMentionCount();

    }

}