package com.wave.backend.workspaceinvite.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceRepository;
import com.wave.backend.workspaceinvite.dto.CreateWorkspaceInvitationRequest;
import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.repository.WorkspaceInvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceInvitationService {

    private final WorkspaceInvitationRepository invitationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    private final WorkspaceInvitationMapper mapper;

    private final WorkspaceInvitationCreateService createService;
    private final WorkspaceInvitationAcceptService acceptService;
    private final WorkspaceInvitationDeclineService declineService;

    public WorkspaceInvitationService(
            WorkspaceInvitationRepository invitationRepository,
            WorkspaceRepository workspaceRepository,
            UserRepository userRepository,
            WorkspaceInvitationMapper mapper,
            WorkspaceInvitationCreateService createService,
            WorkspaceInvitationAcceptService acceptService,
            WorkspaceInvitationDeclineService declineService
    ) {
        this.invitationRepository = invitationRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.createService = createService;
        this.acceptService = acceptService;
        this.declineService = declineService;
    }

    public WorkspaceInvitationResponse createInvitation(
            CreateWorkspaceInvitationRequest request
    ) {

        return createService.createInvitation(request);

    }

    public WorkspaceInvitationResponse acceptInvitation(
            Long invitationId
    ) {

        return acceptService.acceptInvitation(invitationId);

    }

    public WorkspaceInvitationResponse declineInvitation(
            Long invitationId
    ) {

        return declineService.declineInvitation(invitationId);

    }

    public List<WorkspaceInvitationResponse> getMyInvitations() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return invitationRepository
                .findByInviteeOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    public List<WorkspaceInvitationResponse> getWorkspaceInvitations(
            Long workspaceId
    ) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Workspace not found."
                        ));

        return invitationRepository
                .findByWorkspaceOrderByCreatedAtDesc(workspace)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

}