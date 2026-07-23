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
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public ChannelMemberService(
            ChannelMemberRepository channelMemberRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository
    ) {
        this.channelMemberRepository = channelMemberRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    public ChannelMemberResponse joinChannel(Long channelId) {

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        validateWorkspaceMembership(channel, currentUser);

        if (channelMemberRepository.existsByChannelAndUser(channel, currentUser)) {
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

    public void leaveChannel(Long channelId) {

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        ChannelMember member = channelMemberRepository
                .findByChannelAndUser(channel, currentUser)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "You are not a member of this channel."
                        ));

        channelMemberRepository.delete(member);
    }

    public List<ChannelMemberResponse> getMembers(Long channelId) {

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        validateChannelAccess(channel, currentUser);

        return channelMemberRepository.findByChannel(channel)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Public channels:
     *      Any workspace member can access.
     *
     * Private channels:
     *      Only channel members can access.
     */
    public void validateChannelAccess(
            Channel channel,
            User user
    ) {

        validateWorkspaceMembership(channel, user);

        if (!channel.isPrivate()) {
            return;
        }

        if (!channelMemberRepository.existsByChannelAndUser(channel, user)) {
            throw new IllegalArgumentException(
                    "You are not a member of this private channel."
            );
        }

    }

    public boolean isChannelMember(
            Channel channel,
            User user
    ) {
        return channelMemberRepository.existsByChannelAndUser(channel, user);
    }

    public boolean isWorkspaceMember(Long workspaceId, User user) {
        return workspaceMemberRepository.findByUser(user)
                .stream()
                .anyMatch(member ->
                        member.getWorkspace().getId().equals(workspaceId)
                );
    }

    private void validateWorkspaceMembership(
            Channel channel,
            User user
    ) {
        if (!workspaceMemberRepository.existsByWorkspaceAndUser(
                channel.getWorkspace(),
                user
        )) {
            throw new IllegalArgumentException(
                    "You are not a member of this workspace."
            );
        }
    }

    private User getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }

    private ChannelMemberResponse toResponse(ChannelMember member) {

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
