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
import com.wave.backend.message.dto.MessageContextResponse;
import com.wave.backend.message.dto.MessageHistoryResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.mention.service.MentionService;
import com.wave.backend.common.event.MessageCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import com.wave.backend.reaction.dto.ReactionResponse;
import com.wave.backend.reaction.entity.Reaction;
import com.wave.backend.reaction.repository.ReactionRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ChannelMemberService channelMemberService;
    private final MetricsService metricsService;
    private final ReactionRepository reactionRepository;
    private final MentionService mentionService;
    private final ApplicationEventPublisher eventPublisher;

    public MessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            ChannelMemberService channelMemberService,
            MetricsService metricsService,
            ReactionRepository reactionRepository,
            MentionService mentionService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.channelMemberService = channelMemberService;
        this.metricsService = metricsService;
        this.reactionRepository = reactionRepository;
        this.mentionService = mentionService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public MessageResponse sendMessage(CreateMessageRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        return sendMessage(request, email);
    }

    @Transactional
    public MessageResponse sendMessage(
            CreateMessageRequest request,
            String email
    ) {

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

        channelMemberService.validateChannelAccess(channel, sender);

        Message message = new Message();

        message.setContent(request.getContent());
        message.setSender(sender);
        message.setChannel(channel);

        message = messageRepository.save(message);
        mentionService.processChannelMentions(message);

        metricsService.incrementMessageSent();
        MessageResponse response = toResponse(message, sender);
        eventPublisher.publishEvent(new MessageCreatedEvent(response));
        return response;
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getChannelMessages(Long channelId) {
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

        channelMemberService.validateChannelAccess(channel, currentUser);

        return messageRepository
                .findByChannelAndParentMessageIsNullOrderByCreatedAtAsc(channel)
                .stream()
                .map(message -> toResponse(message, currentUser))
                .toList();
    }

    @Transactional(readOnly = true)
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

        channelMemberService.validateChannelAccess(channel, currentUser);

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
                .map(message -> toResponse(message, currentUser))
                .toList();
    }

    @Transactional(readOnly = true)
    public MessageHistoryResponse getChannelHistory(
            Long channelId,
            Long beforeId,
            int requestedSize
    ) {
        String email = SecurityUtil.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));
        workspaceMemberRepository
                .findByWorkspaceAndUser(channel.getWorkspace(), currentUser)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));
        channelMemberService.validateChannelAccess(channel, currentUser);

        int size = Math.min(Math.max(requestedSize, 1), 100);
        List<Message> fetched = messageRepository.findHistory(
                channel,
                beforeId,
                PageRequest.of(0, size + 1)
        );
        boolean hasMore = fetched.size() > size;
        List<Message> page = new ArrayList<>(
                fetched.subList(0, Math.min(size, fetched.size()))
        );
        Long nextCursor = hasMore && !page.isEmpty()
                ? page.get(page.size() - 1).getId()
                : null;
        Collections.reverse(page);
        return new MessageHistoryResponse(
                page.stream()
                        .map(message -> toResponse(message, currentUser))
                        .toList(),
                nextCursor,
                hasMore
        );
    }

    @Transactional(readOnly = true)
    public MessageContextResponse getMessageContext(Long messageId) {
        String email = SecurityUtil.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new com.wave.backend.exception.MessageNotFoundException(
                        "Message not found."
                ));
        channelMemberService.validateChannelAccess(message.getChannel(), currentUser);
        return new MessageContextResponse(
                message.getId(),
                message.getChannel().getId(),
                message.getChannel().getWorkspace().getId()
        );
    }

    private MessageResponse toResponse(
            Message message,
            User currentUser
    ) {

        final Long messageId = message.getId();

        List<Reaction> reactions =
                reactionRepository.findAllByMessageOrderByEmojiAsc(message);

        Map<String, ReactionResponse> grouped = new LinkedHashMap<>();

        for (Reaction reaction : reactions) {

            ReactionResponse response = grouped.computeIfAbsent(
                    reaction.getEmoji(),
                    emoji -> new ReactionResponse(
                            messageId,
                            emoji,
                            0,
                            false
                    )
            );

            response.setCount(response.getCount() + 1);

            if (reaction.getUser().getId().equals(currentUser.getId())) {
                response.setReactedByCurrentUser(true);
            }
        }

        return new MessageResponse(
                messageId,
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getChannel().getId(),
                message.getContent(),
                message.isEdited(),
                message.isDeleted(),
                List.copyOf(grouped.values()),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
