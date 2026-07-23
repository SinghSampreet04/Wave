package com.wave.backend.websocket.controller;

import com.wave.backend.websocket.dto.PresenceMessage;
import com.wave.backend.websocket.dto.UpdatePresenceRequest;
import com.wave.backend.websocket.service.PresenceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/presence")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @GetMapping("/workspaces/{workspaceId}")
    public List<PresenceMessage> workspacePresence(
            @PathVariable Long workspaceId
    ) {
        return presenceService.getWorkspacePresence(workspaceId);
    }

    @PatchMapping("/me")
    public PresenceMessage updateStatus(
            @Valid @RequestBody UpdatePresenceRequest request
    ) {
        return presenceService.updateMyStatus(request.getStatus());
    }
}
