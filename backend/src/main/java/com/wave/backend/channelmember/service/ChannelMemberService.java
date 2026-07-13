package com.wave.backend.channelmember.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.dto.ChannelMemberResponse;
import com.wave.backend.channelmember.entity.ChannelMember;
import com.wave.backend.channelmember.repository.ChannelMemberRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public ChannelMemberService(
            ChannelMemberRepository channelMemberRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository
    ) {
        this.channelMemberRepository = channelMemberRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    public ChannelMemberResponse joinChannel(
            Long channelId
    ) {

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException(
                                "Channel not found."
                        ));

        if (channelMemberRepository.existsByChannelAndUser(
                channel,
                currentUser
        )) {

            throw new IllegalArgumentException(
                    "You are already a member of this channel."
            );

        }

        ChannelMember member = new ChannelMember();

        member.setChannel(channel);
        member.setUser(currentUser);

        member = channelMemberRepository.save(member);

        return toResponse(member);

    }

    public void leaveChannel(
            Long channelId
    ) {

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException(
                                "Channel not found."
                        ));

        ChannelMember member =
                channelMemberRepository
                        .findByChannelAndUser(
                                channel,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "You are not a member of this channel."
                                ));

        channelMemberRepository.delete(member);

    }

    public List<ChannelMemberResponse> getMembers(
            Long channelId
    ) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException(
                                "Channel not found."
                        ));

        return channelMemberRepository
                .findByChannel(channel)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public boolean isChannelMember(
            Channel channel,
            User user
    ) {

        return channelMemberRepository.existsByChannelAndUser(
                channel,
                user
        );

    }

    public void requireChannelMember(
            Channel channel,
            User user
    ) {

        if (!isChannelMember(channel, user)) {

            throw new IllegalArgumentException(
                    "Access denied."
            );

        }

    }

    private User getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

    }

    private ChannelMemberResponse toResponse(
            ChannelMember member
    ) {

        return new ChannelMemberResponse(
                member.getId(),
                member.getChannel().getId(),
                member.getChannel().getName(),
                member.getUser().getId(),
                member.getUser().getUsername(),
                member.getJoinedAt()
        );

    }

}