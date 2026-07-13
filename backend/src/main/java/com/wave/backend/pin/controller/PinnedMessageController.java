package com.wave.backend.pin.controller;

import com.wave.backend.pin.dto.PinnedMessageResponse;
import com.wave.backend.pin.service.PinnedMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PinnedMessageController {

    private final PinnedMessageService pinnedMessageService;

    public PinnedMessageController(
            PinnedMessageService pinnedMessageService
    ) {
        this.pinnedMessageService = pinnedMessageService;
    }

    @PostMapping("/messages/{messageId}/pin")
    public PinnedMessageResponse pinMessage(
            @PathVariable Long messageId
    ) {

        return pinnedMessageService.pinMessage(messageId);

    }

    @DeleteMapping("/messages/{messageId}/pin")
    public void unpinMessage(
            @PathVariable Long messageId
    ) {

        pinnedMessageService.unpinMessage(messageId);

    }

    @GetMapping("/channels/{channelId}/pins")
    public List<PinnedMessageResponse> getPinnedMessages(
            @PathVariable Long channelId
    ) {

        return pinnedMessageService.getPinnedMessages(channelId);

    }

}