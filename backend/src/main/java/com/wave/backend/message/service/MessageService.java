package com.wave.backend.message.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.WorkspaceAccessDeniedException;
import com.wave.backend.message.dto.CreateMessageRequest;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public MessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
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
}