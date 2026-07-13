package com.wave.backend.channelinvite.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.repository.ChannelMemberRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.entity.WorkspaceMember;
import com.wave.backend.workspace.entity.WorkspaceRole;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.channelinvite.dto.ChannelInvitationResponse;
import com.wave.backend.channelinvite.dto.CreateChannelInvitationRequest;
import com.wave.backend.channelinvite.entity.ChannelInvitation;
import com.wave.backend.channelinvite.entity.ChannelInvitationStatus;
import com.wave.backend.channelinvite.repository.ChannelInvitationRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelInvitationCreateService {

    private final ChannelInvitationRepository invitationRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ChannelInvitationMapper mapper;

    public ChannelInvitationCreateService(
            ChannelInvitationRepository invitationRepository,
            ChannelRepository channelRepository,
            ChannelMemberRepository channelMemberRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            ChannelInvitationMapper mapper
    ) {
        this.invitationRepository = invitationRepository;
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public ChannelInvitationResponse createInvitation(
            CreateChannelInvitationRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User inviter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Channel channel = channelRepository.findById(
                        request.getChannelId())
                .orElseThrow(() ->
                        new ChannelNotFoundException(
                                "Channel not found."
                        ));

        if (!channel.isPrivate()) {

            throw new IllegalArgumentException(
                    "Invitations are only supported for private channels."
            );

        }

        User invitee = userRepository.findById(
                        request.getInviteeId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Invitee not found."
                        ));

        if (inviter.getId().equals(invitee.getId())) {

            throw new IllegalArgumentException(
                    "You cannot invite yourself."
            );

        }

        Workspace workspace = channel.getWorkspace();

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

        workspaceMemberRepository
                .findByWorkspaceAndUser(
                        workspace,
                        invitee
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invitee is not a workspace member."
                        ));

        if (channelMemberRepository.existsByChannelAndUser(
                channel,
                invitee
        )) {

            throw new IllegalArgumentException(
                    "User is already a channel member."
            );

        }

        if (invitationRepository.existsByChannelAndInviteeAndStatus(
                channel,
                invitee,
                ChannelInvitationStatus.PENDING
        )) {

            throw new IllegalArgumentException(
                    "A pending invitation already exists."
            );

        }

        ChannelInvitation invitation =
                new ChannelInvitation();

        invitation.setChannel(channel);
        invitation.setInviter(inviter);
        invitation.setInvitee(invitee);

        invitation = invitationRepository.save(
                invitation
        );

        notificationService.createNotification(
                invitee,
                NotificationType.CHANNEL_INVITE,
                "Channel Invitation",
                inviter.getUsername()
                        + " invited you to #"
                        + channel.getName(),
                channel.getId(),
                "CHANNEL"
        );

        // Future:
        // emailService.sendChannelInvitation(...);

        return mapper.toResponse(invitation);

    }

}