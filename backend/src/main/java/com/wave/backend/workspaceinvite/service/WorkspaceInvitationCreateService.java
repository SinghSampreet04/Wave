package com.wave.backend.workspaceinvite.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.entity.WorkspaceMember;
import com.wave.backend.workspace.entity.WorkspaceRole;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.workspace.repository.WorkspaceRepository;
import com.wave.backend.workspaceinvite.dto.CreateWorkspaceInvitationRequest;
import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitation;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitationStatus;
import com.wave.backend.workspaceinvite.repository.WorkspaceInvitationRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceInvitationCreateService {

    private final WorkspaceInvitationRepository invitationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final WorkspaceInvitationMapper mapper;

    public WorkspaceInvitationCreateService(
            WorkspaceInvitationRepository invitationRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            WorkspaceInvitationMapper mapper
    ) {
        this.invitationRepository = invitationRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public WorkspaceInvitationResponse createInvitation(
            CreateWorkspaceInvitationRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User inviter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Workspace workspace = workspaceRepository
                .findById(request.getWorkspaceId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Workspace not found."
                        ));

        User invitee = userRepository
                .findById(request.getInviteeId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Invitee not found."
                        ));

        WorkspaceMember inviterMember =
                workspaceMemberRepository
                        .findByWorkspaceAndUser(
                                workspace,
                                inviter
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "You are not a workspace member."
                                ));

        if (inviterMember.getRole() != WorkspaceRole.OWNER
                && inviterMember.getRole() != WorkspaceRole.ADMIN) {

            throw new IllegalArgumentException(
                    "Only owners and admins can invite users."
            );

        }

        if (inviter.getId().equals(invitee.getId())) {

            throw new IllegalArgumentException(
                    "You cannot invite yourself."
            );

        }

        if (workspaceMemberRepository.existsByWorkspaceAndUser(
                workspace,
                invitee
        )) {

            throw new IllegalArgumentException(
                    "User is already a workspace member."
            );

        }

        invitationRepository
                .findByWorkspaceAndInviteeAndStatus(
                        workspace,
                        invitee,
                        WorkspaceInvitationStatus.PENDING
                )
                .ifPresent(invitation -> {
                    throw new IllegalArgumentException(
                            "A pending invitation already exists."
                    );
                });

        WorkspaceInvitation invitation =
                new WorkspaceInvitation();

        invitation.setWorkspace(workspace);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);

        invitation = invitationRepository.save(
                invitation
        );

        notificationService.createNotification(
                invitee,
                NotificationType.WORKSPACE_INVITE,
                "Workspace Invitation",
                inviter.getUsername()
                        + " invited you to "
                        + workspace.getName(),
                workspace.getId(),
                "WORKSPACE"
        );

        // Future:
        // emailService.sendWorkspaceInvitation(...);

        return mapper.toResponse(invitation);

    }

}