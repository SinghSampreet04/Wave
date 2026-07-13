package com.wave.backend.message.controller;

import com.wave.backend.message.dto.ReplyMessageRequest;
import com.wave.backend.message.dto.ThreadResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.service.ThreadService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadController {

    private final ThreadService threadService;

    public ThreadController(
            ThreadService threadService
    ) {
        this.threadService = threadService;
    }

    @PostMapping("/reply")
    public ThreadResponse reply(
            @Valid @RequestBody ReplyMessageRequest request
    ) {
        return threadService.reply(request);
    }

    @GetMapping("/{messageId}")
    public List<Message> getReplies(
            @PathVariable Long messageId
    ) {
        return threadService.getReplies(messageId);
    }

}