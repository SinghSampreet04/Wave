package com.wave.backend.workspaceinvite.controller;

import com.wave.backend.workspaceinvite.dto.CreateWorkspaceInvitationRequest;
import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.service.WorkspaceInvitationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workspace-invitations")
public class WorkspaceInvitationController {

    private final WorkspaceInvitationService workspaceInvitationService;

    public WorkspaceInvitationController(
            WorkspaceInvitationService workspaceInvitationService
    ) {
        this.workspaceInvitationService = workspaceInvitationService;
    }

    @PostMapping
    public WorkspaceInvitationResponse createInvitation(
            @Valid @RequestBody CreateWorkspaceInvitationRequest request
    ) {

        return workspaceInvitationService.createInvitation(request);

    }

    @GetMapping("/me")
    public List<WorkspaceInvitationResponse> getMyInvitations() {

        return workspaceInvitationService.getMyInvitations();

    }

    @GetMapping("/workspace/{workspaceId}")
    public List<WorkspaceInvitationResponse> getWorkspaceInvitations(
            @PathVariable Long workspaceId
    ) {

        return workspaceInvitationService.getWorkspaceInvitations(
                workspaceId
        );

    }

    @PatchMapping("/{invitationId}/accept")
    public WorkspaceInvitationResponse acceptInvitation(
            @PathVariable Long invitationId
    ) {

        return workspaceInvitationService.acceptInvitation(
                invitationId
        );

    }

    @PatchMapping("/{invitationId}/decline")
    public WorkspaceInvitationResponse declineInvitation(
            @PathVariable Long invitationId
    ) {

        return workspaceInvitationService.declineInvitation(
                invitationId
        );

    }

}