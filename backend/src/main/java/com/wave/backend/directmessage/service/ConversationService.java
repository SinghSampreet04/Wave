package com.wave.backend.directmessage.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.ConversationResponse;
import com.wave.backend.directmessage.dto.CreateConversationRequest;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.directmessage.read.service.ConversationReadService;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationReadService conversationReadService;

    public ConversationService(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ConversationReadService conversationReadService
    ) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.conversationReadService = conversationReadService;
    }

    @Transactional
    public ConversationResponse createConversation(
            CreateConversationRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        User recipient = userRepository.findById(
                        request.getRecipientUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("Recipient not found."));

        if (currentUser.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException(
                    "You cannot create a conversation with yourself."
            );
        }

        Conversation existing = conversationRepository
                .findByUserOneAndUserTwo(currentUser, recipient)
                .or(() ->
                        conversationRepository.findByUserTwoAndUserOne(
                                currentUser,
                                recipient
                        ))
                .orElse(null);

        if (existing != null) {
            return toResponse(existing, currentUser);
        }

        Conversation conversation = new Conversation();

        conversation.setUserOne(currentUser);
        conversation.setUserTwo(recipient);

        conversation = conversationRepository.save(conversation);

        return toResponse(conversation, currentUser);

    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getMyConversations() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return conversationRepository
                .findByUserOneOrUserTwoOrderByLastMessageAtDesc(
                        currentUser,
                        currentUser
                )
                .stream()
                .map(conversation ->
                        toResponse(conversation, currentUser)
                )
                .toList();

    }

    private ConversationResponse toResponse(
            Conversation conversation,
            User currentUser
    ) {

        User otherUser;

        if (conversation.getUserOne().getId()
                .equals(currentUser.getId())) {

            otherUser = conversation.getUserTwo();

        } else {

            otherUser = conversation.getUserOne();

        }

        return new ConversationResponse(
                conversation.getId(),
                otherUser.getId(),
                otherUser.getUsername(),
                otherUser.getFirstName(),
                otherUser.getLastName(),
                otherUser.getAvatar(),
                conversation.getLastMessage(),
                conversation.getLastMessageAt(),
                conversation.getCreatedAt(),
                conversationReadService.getUnreadCount(
                        conversation,
                        currentUser
                )
        );

    }

}
