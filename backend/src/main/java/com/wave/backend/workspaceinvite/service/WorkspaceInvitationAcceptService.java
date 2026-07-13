package com.wave.backend.workspaceinvite.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.WorkspaceMember;
import com.wave.backend.workspace.entity.WorkspaceRole;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitation;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitationStatus;
import com.wave.backend.workspaceinvite.repository.WorkspaceInvitationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WorkspaceInvitationAcceptService {

    private final WorkspaceInvitationRepository invitationRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final WorkspaceInvitationMapper mapper;

    public WorkspaceInvitationAcceptService(
            WorkspaceInvitationRepository invitationRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            WorkspaceInvitationMapper mapper
    ) {
        this.invitationRepository = invitationRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public WorkspaceInvitationResponse acceptInvitation(
            Long invitationId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        WorkspaceInvitation invitation =
                invitationRepository.findById(invitationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invitation not found."
                                ));

        if (!invitation.getInvitee().getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You cannot accept this invitation."
            );

        }

        if (invitation.getStatus()
                != WorkspaceInvitationStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Invitation has already been processed."
            );

        }

        if (invitation.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            invitation.setStatus(
                    WorkspaceInvitationStatus.EXPIRED
            );

            invitationRepository.save(invitation);

            throw new IllegalArgumentException(
                    "Invitation has expired."
            );

        }

        if (!workspaceMemberRepository.existsByWorkspaceAndUser(
                invitation.getWorkspace(),
                currentUser
        )) {

            WorkspaceMember member =
                    new WorkspaceMember();

            member.setWorkspace(
                    invitation.getWorkspace()
            );

            member.setUser(currentUser);

            member.setRole(
                    WorkspaceRole.MEMBER
            );

            workspaceMemberRepository.save(member);

        }

        invitation.setStatus(
                WorkspaceInvitationStatus.ACCEPTED
        );

        invitation.setRespondedAt(
                LocalDateTime.now()
        );

        invitation = invitationRepository.save(
                invitation
        );

        notificationService.createNotification(
                invitation.getInviter(),
                NotificationType.WORKSPACE_INVITE,
                "Invitation Accepted",
                currentUser.getUsername()
                        + " joined "
                        + invitation.getWorkspace().getName(),
                invitation.getWorkspace().getId(),
                "WORKSPACE"
        );

        return mapper.toResponse(invitation);

    }

}