package com.wave.backend.channelinvite.service;

import com.wave.backend.channelinvite.dto.ChannelInvitationResponse;
import com.wave.backend.channelinvite.entity.ChannelInvitation;
import org.springframework.stereotype.Component;

@Component
public class ChannelInvitationMapper {

    public ChannelInvitationResponse toResponse(
            ChannelInvitation invitation
    ) {

        return new ChannelInvitationResponse(
                invitation.getId(),
                invitation.getChannel().getId(),
                invitation.getChannel().getName(),
                invitation.getInviter().getId(),
                invitation.getInviter().getUsername(),
                invitation.getInvitee().getId(),
                invitation.getInvitee().getUsername(),
                invitation.getStatus(),
                invitation.getCreatedAt(),
                invitation.getExpiresAt(),
                invitation.getRespondedAt()
        );

    }

}