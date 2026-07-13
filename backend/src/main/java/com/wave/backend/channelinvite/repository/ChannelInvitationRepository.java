package com.wave.backend.channelinvite.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channelinvite.entity.ChannelInvitation;
import com.wave.backend.channelinvite.entity.ChannelInvitationStatus;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelInvitationRepository
        extends JpaRepository<ChannelInvitation, Long> {

    List<ChannelInvitation> findByInviteeOrderByCreatedAtDesc(
            User invitee
    );

    List<ChannelInvitation> findByChannelOrderByCreatedAtDesc(
            Channel channel
    );

    Optional<ChannelInvitation> findByChannelAndInviteeAndStatus(
            Channel channel,
            User invitee,
            ChannelInvitationStatus status
    );

    List<ChannelInvitation> findByChannelAndInviter(
            Channel channel,
            User inviter
    );

    boolean existsByChannelAndInviteeAndStatus(
            Channel channel,
            User invitee,
            ChannelInvitationStatus status
    );

}