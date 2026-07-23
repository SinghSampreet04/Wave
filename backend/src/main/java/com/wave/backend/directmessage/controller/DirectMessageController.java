package com.wave.backend.directmessage.controller;

import com.wave.backend.directmessage.dto.DirectMessageDeleteResponse;
import com.wave.backend.directmessage.dto.DirectMessageEditResponse;
import com.wave.backend.directmessage.dto.DirectMessageResponse;
import com.wave.backend.directmessage.dto.DirectMessageContextResponse;
import com.wave.backend.directmessage.dto.DirectMessageSearchResponse;
import com.wave.backend.directmessage.dto.SendDirectMessageRequest;
import com.wave.backend.directmessage.dto.UpdateDirectMessageRequest;
import com.wave.backend.directmessage.service.DirectMessageDeleteService;
import com.wave.backend.directmessage.service.DirectMessageEditService;
import com.wave.backend.directmessage.service.DirectMessageSearchService;
import com.wave.backend.directmessage.service.DirectMessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/direct-messages")
public class DirectMessageController {

    private final DirectMessageService directMessageService;
    private final DirectMessageEditService directMessageEditService;
    private final DirectMessageDeleteService directMessageDeleteService;
    private final DirectMessageSearchService directMessageSearchService;

    public DirectMessageController(
            DirectMessageService directMessageService,
            DirectMessageEditService directMessageEditService,
            DirectMessageDeleteService directMessageDeleteService,
            DirectMessageSearchService directMessageSearchService
    ) {
        this.directMessageService = directMessageService;
        this.directMessageEditService = directMessageEditService;
        this.directMessageDeleteService = directMessageDeleteService;
        this.directMessageSearchService = directMessageSearchService;
    }

    @PostMapping
    public DirectMessageResponse sendMessage(
            @Valid @RequestBody SendDirectMessageRequest request
    ) {

        return directMessageService.sendMessage(request);

    }

    @GetMapping("/conversation/{conversationId}")
    public List<DirectMessageResponse> getConversationMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {

        return directMessageService.getConversationMessages(
                conversationId,
                page,
                size
        );

    }

    @GetMapping("/conversation/{conversationId}/search")
    public List<DirectMessageSearchResponse> search(
            @PathVariable Long conversationId,
            @RequestParam String query
    ) {

        return directMessageSearchService.search(
                conversationId,
                query
        );

    }

    @GetMapping("/{messageId}/context")
    public DirectMessageContextResponse getMessageContext(
            @PathVariable Long messageId
    ) {
        return directMessageService.getMessageContext(messageId);
    }

    @PatchMapping("/{messageId}")
    public DirectMessageEditResponse editMessage(
            @PathVariable Long messageId,
            @Valid @RequestBody UpdateDirectMessageRequest request
    ) {

        return directMessageEditService.editMessage(
                messageId,
                request
        );

    }

    @DeleteMapping("/{messageId}")
    public DirectMessageDeleteResponse deleteMessage(
            @PathVariable Long messageId
    ) {

        return directMessageDeleteService.deleteMessage(
                messageId
        );

    }

}
