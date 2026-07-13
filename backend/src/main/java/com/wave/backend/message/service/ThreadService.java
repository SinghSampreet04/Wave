package com.wave.backend.message.service;

import com.wave.backend.common.event.ThreadReplyEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.WorkspaceAccessDeniedException;
import com.wave.backend.message.dto.ReplyMessageRequest;
import com.wave.backend.message.dto.ThreadResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ThreadService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.eventPublisher = eventPublisher;
    }

    public ThreadResponse reply(
            ReplyMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Message parent = messageRepository.findById(
                        request.getParentMessageId())
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        Workspace workspace = parent
                .getChannel()
                .getWorkspace();

        workspaceMemberRepository
                .findByWorkspaceAndUser(workspace, sender)
                .orElseThrow(() ->
                        new WorkspaceAccessDeniedException(
                                "Access denied."));

        Message reply = new Message();

        reply.setContent(request.getContent());
        reply.setSender(sender);
        reply.setChannel(parent.getChannel());
        reply.setParentMessage(parent);

        reply = messageRepository.save(reply);

        eventPublisher.publishEvent(
                new ThreadReplyEvent(
                        reply.getId(),
                        parent.getId(),
                        parent.getChannel().getId(),
                        sender.getId(),
                        sender.getUsername(),
                        reply.getContent(),
                        reply.getCreatedAt()
                )
        );

        return new ThreadResponse(
                reply.getId(),
                parent.getId(),
                sender.getId(),
                sender.getUsername(),
                reply.getContent(),
                reply.getCreatedAt()
        );
    }

    public List<Message> getReplies(
            Long parentMessageId
    ) {

        Message parent = messageRepository.findById(parentMessageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        return messageRepository.findByParentMessageOrderByCreatedAtAsc(parent);
    }

}