package com.wave.backend.message.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.common.event.MessageEditedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.WorkspaceAccessDeniedException;
import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.dto.MessageEditResponse;
import com.wave.backend.message.dto.UpdateMessageRequest;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.eventPublisher = eventPublisher;
    }

    public Message sendMessage(CreateMessageRequest request) {

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

        Message message = new Message();

        message.setContent(request.getContent());
        message.setSender(sender);
        message.setChannel(channel);

        return messageRepository.save(message);
    }

    public List<Message> getChannelMessages(Long channelId) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        Workspace workspace = channel.getWorkspace();

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, user)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException("Access denied."));

        return messageRepository.findByChannelOrderByCreatedAtAsc(channel);
    }

    public MessageEditResponse editMessage(
            Long messageId,
            UpdateMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        if (!message.getSender().getId().equals(currentUser.getId())) {
            throw new WorkspaceAccessDeniedException(
                    "You can only edit your own messages."
            );
        }

        message.setContent(request.getContent());
        message.setEdited(true);
        message.setEditedAt(LocalDateTime.now());

        message = messageRepository.save(message);

        eventPublisher.publishEvent(
                new MessageEditedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        message.getContent(),
                        message.getEditedAt()
                )
        );

        return new MessageEditResponse(
                message.getId(),
                message.getChannel().getId(),
                message.getContent(),
                message.isEdited(),
                message.getEditedAt()
        );
    }

}