package com.wave.backend.channelinvite.service;

import com.wave.backend.channelinvite.dto.ChannelInvitationResponse;
import com.wave.backend.channelinvite.entity.ChannelInvitation;
import com.wave.backend.channelinvite.entity.ChannelInvitationStatus;
import com.wave.backend.channelinvite.repository.ChannelInvitationRepository;
import com.wave.backend.channelmember.entity.ChannelMember;
import com.wave.backend.channelmember.repository.ChannelMemberRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ChannelInvitationAcceptService {

    private final ChannelInvitationRepository invitationRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ChannelInvitationMapper mapper;

    public ChannelInvitationAcceptService(
            ChannelInvitationRepository invitationRepository,
            ChannelMemberRepository channelMemberRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            ChannelInvitationMapper mapper
    ) {
        this.invitationRepository = invitationRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    public ChannelInvitationResponse accept(
            Long invitationId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        ChannelInvitation invitation =
                invitationRepository.findById(invitationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invitation not found."
                                ));

        if (!invitation.getInvitee().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You cannot accept this invitation."
            );

        }

        if (invitation.getStatus() != ChannelInvitationStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Invitation has already been processed."
            );

        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {

            invitation.setStatus(
                    ChannelInvitationStatus.EXPIRED
            );

            invitationRepository.save(invitation);

            throw new IllegalArgumentException(
                    "Invitation has expired."
            );

        }

        if (!channelMemberRepository.existsByChannelAndUser(
                invitation.getChannel(),
                currentUser
        )) {

            ChannelMember member =
                    new ChannelMember();

            member.setChannel(
                    invitation.getChannel()
            );

            member.setUser(currentUser);

            channelMemberRepository.save(member);

        }

        invitation.setStatus(
                ChannelInvitationStatus.ACCEPTED
        );

        invitation.setRespondedAt(
                LocalDateTime.now()
        );

        invitationRepository.save(invitation);

        notificationService.createNotification(
                invitation.getInviter(),
                NotificationType.CHANNEL_INVITE,
                "Invitation Accepted",
                currentUser.getUsername()
                        + " accepted your invitation to #"
                        + invitation.getChannel().getName(),
                invitation.getChannel().getId(),
                "CHANNEL"
        );

        return mapper.toResponse(invitation);

    }

}
