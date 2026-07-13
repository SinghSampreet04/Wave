package com.wave.backend.channelmember.repository;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channelmember.entity.ChannelMember;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository
        extends JpaRepository<ChannelMember, Long> {

    List<ChannelMember> findByChannel(
            Channel channel
    );

    List<ChannelMember> findByUser(
            User user
    );

    Optional<ChannelMember> findByChannelAndUser(
            Channel channel,
            User user
    );

    boolean existsByChannelAndUser(
            Channel channel,
            User user
    );

}