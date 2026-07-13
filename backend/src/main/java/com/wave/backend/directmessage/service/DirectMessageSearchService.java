package com.wave.backend.directmessage.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.DirectMessageSearchResponse;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectMessageSearchService {

    private final DirectMessageRepository directMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public DirectMessageSearchService(
            DirectMessageRepository directMessageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository
    ) {
        this.directMessageRepository = directMessageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public List<DirectMessageSearchResponse> search(
            Long conversationId,
            String query
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Conversation conversation =
                conversationRepository.findById(conversationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Conversation not found."
                                ));

        boolean participant =
                conversation.getUserOne().getId().equals(currentUser.getId())
                        || conversation.getUserTwo().getId().equals(currentUser.getId());

        if (!participant) {

            throw new IllegalArgumentException(
                    "Access denied."
            );

        }

        return directMessageRepository
                .findByConversationAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
                        conversation,
                        query
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private DirectMessageSearchResponse toResponse(
            DirectMessage message
    ) {

        return new DirectMessageSearchResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.isEdited(),
                message.isDeleted(),
                message.getCreatedAt()
        );

    }

}