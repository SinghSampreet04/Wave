package com.wave.backend.channel.service;

import com.wave.backend.channel.dto.ChannelResponse;
import com.wave.backend.channel.dto.CreateChannelRequest;
import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.entity.ChannelMember;
import com.wave.backend.channelmember.repository.ChannelMemberRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.WorkspaceAccessDeniedException;
import com.wave.backend.exception.WorkspaceNotFoundException;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;
    private final MetricsService metricsService;

    public ChannelService(
            ChannelRepository channelRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            ChannelMemberRepository channelMemberRepository,
            UserRepository userRepository,
            MetricsService metricsService
    ) {
        this.channelRepository = channelRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.userRepository = userRepository;
        this.metricsService = metricsService;
    }

    @Transactional
    public ChannelResponse createChannel(CreateChannelRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() ->
                        new WorkspaceNotFoundException("Workspace not found."));

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, user)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));

        Channel channel = new Channel();

        channel.setName(request.getName());
        channel.setDescription(request.getDescription());
        channel.setWorkspace(workspace);
        channel.setPrivate(request.isPrivate());

        channel = channelRepository.save(channel);

        // Automatically add the creator as a channel member
        ChannelMember member = new ChannelMember();
        member.setChannel(channel);
        member.setUser(user);

        channelMemberRepository.save(member);

        metricsService.incrementChannelCreated();

        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.isPrivate(),
                workspace.getId()
        );
    }

    @Transactional(readOnly = true)
    public List<ChannelResponse> getWorkspaceChannels(Long workspaceId) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new WorkspaceNotFoundException("Workspace not found."));

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, user)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));

        return channelRepository.findByWorkspace(workspace)
                .stream()
                .map(channel -> new ChannelResponse(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.isPrivate(),
                        workspace.getId()
                ))
                .toList();
    }

}