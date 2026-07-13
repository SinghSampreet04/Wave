package com.wave.backend.directmessage.service;

import com.wave.backend.common.event.DirectMessageSentEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.DirectMessageResponse;
import com.wave.backend.directmessage.dto.SendDirectMessageRequest;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DirectMessageService(
            DirectMessageRepository directMessageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.directMessageRepository = directMessageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public DirectMessageResponse sendMessage(
            SendDirectMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Conversation conversation = conversationRepository
                .findById(request.getConversationId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conversation not found."
                        ));

        if (!conversation.getUserOne().getId().equals(sender.getId())
                && !conversation.getUserTwo().getId().equals(sender.getId())) {

            throw new IllegalArgumentException(
                    "You are not part of this conversation."
            );

        }

        DirectMessage message = new DirectMessage();

        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());

        message = directMessageRepository.save(message);

        conversation.setLastMessage(message.getContent());
        conversation.setLastMessageAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        eventPublisher.publishEvent(
                new DirectMessageSentEvent(
                        message.getId(),
                        conversation.getId(),
                        sender.getId(),
                        sender.getUsername(),
                        message.getContent(),
                        message.getCreatedAt()
                )
        );

        return toResponse(message);

    }

    public List<DirectMessageResponse> getConversationMessages(
            Long conversationId,
            int page,
            int size
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conversation not found."
                        ));

        if (!conversation.getUserOne().getId().equals(currentUser.getId())
                && !conversation.getUserTwo().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You are not part of this conversation."
            );

        }

        return directMessageRepository
                .findByConversationOrderByCreatedAtDesc(
                        conversation,
                        PageRequest.of(page, size)
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private DirectMessageResponse toResponse(
            DirectMessage message
    ) {

        return new DirectMessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.isEdited(),
                message.isDeleted(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );

    }

}