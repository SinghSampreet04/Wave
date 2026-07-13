package com.wave.backend.workspaceinvite.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitation;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitationStatus;
import com.wave.backend.workspaceinvite.repository.WorkspaceInvitationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WorkspaceInvitationDeclineService {

    private final WorkspaceInvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final WorkspaceInvitationMapper mapper;

    public WorkspaceInvitationDeclineService(
            WorkspaceInvitationRepository invitationRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            WorkspaceInvitationMapper mapper
    ) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public WorkspaceInvitationResponse declineInvitation(
            Long invitationId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

        WorkspaceInvitation invitation =
                invitationRepository.findById(invitationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invitation not found."
                                ));

        if (!invitation.getInvitee().getId()
                .equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You cannot decline this invitation."
            );

        }

        if (invitation.getStatus()
                != WorkspaceInvitationStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Invitation has already been processed."
            );

        }

        invitation.setStatus(
                WorkspaceInvitationStatus.DECLINED
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
                "Invitation Declined",
                currentUser.getUsername()
                        + " declined your invitation to "
                        + invitation.getWorkspace().getName(),
                invitation.getWorkspace().getId(),
                "WORKSPACE"
        );

        return mapper.toResponse(
                invitation
        );

    }

}