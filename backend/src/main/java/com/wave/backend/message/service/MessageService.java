package com.wave.backend.message.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.service.ChannelMemberService;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.WorkspaceAccessDeniedException;
import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ChannelMemberService channelMemberService;
    private final MetricsService metricsService;

    public MessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            ChannelMemberService channelMemberService,
            MetricsService metricsService
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.channelMemberService = channelMemberService;
        this.metricsService = metricsService;
    }

    public MessageResponse sendMessage(
            CreateMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        Workspace workspace = channel.getWorkspace();

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, sender)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));

        channelMemberService.validateChannelAccess(
                channel,
                sender
        );

        Message message = new Message();

        message.setContent(request.getContent());
        message.setSender(sender);
        message.setChannel(channel);

        message = messageRepository.save(message);

        metricsService.incrementMessageSent();

        return toResponse(message);

    }

    public List<MessageResponse> getChannelMessages(
            Long channelId
    ) {
        return getChannelMessages(channelId, 0, 20);
    }

    public List<MessageResponse> getChannelMessages(
            Long channelId,
            int page,
            int size
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        Workspace workspace = channel.getWorkspace();

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, currentUser)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));

        channelMemberService.validateChannelAccess(
                channel,
                currentUser
        );

        return messageRepository
                .findByChannelOrderByCreatedAtDesc(
                        channel,
                        PageRequest.of(
                                page,
                                size,
                                Sort.by("createdAt").descending()
                        )
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private MessageResponse toResponse(
            Message message
    ) {

        return new MessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getChannel().getId(),
                message.getContent(),
                message.isEdited(),
                message.isDeleted(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );

    }

}