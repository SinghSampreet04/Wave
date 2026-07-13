package com.wave.backend.directmessage.read.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.read.entity.ConversationReadState;
import com.wave.backend.directmessage.read.repository.ConversationReadStateRepository;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationReadService {

    private final ConversationReadStateRepository readStateRepository;
    private final ConversationRepository conversationRepository;
    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    public ConversationReadService(
            ConversationReadStateRepository readStateRepository,
            ConversationRepository conversationRepository,
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository
    ) {
        this.readStateRepository = readStateRepository;
        this.conversationRepository = conversationRepository;
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    public void markConversationAsRead(
            Long conversationId,
            Long messageId
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

        DirectMessage message = directMessageRepository
                .findById(messageId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Message not found."
                        ));

        ConversationReadState readState =
                readStateRepository
                        .findByConversationAndUser(
                                conversation,
                                currentUser
                        )
                        .orElseGet(() -> {

                            ConversationReadState state =
                                    new ConversationReadState();

                            state.setConversation(conversation);
                            state.setUser(currentUser);

                            return state;

                        });

        readState.setLastReadMessage(message);

        readStateRepository.save(readState);

    }

    public long getUnreadCount(
            Long conversationId
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

        ConversationReadState readState =
                readStateRepository
                        .findByConversationAndUser(
                                conversation,
                                currentUser
                        )
                        .orElse(null);

        if (readState == null ||
                readState.getLastReadMessage() == null) {

            return directMessageRepository
                    .findByConversationOrderByCreatedAtAsc(
                            conversation
                    )
                    .size();

        }

        Long lastReadId =
                readState
                        .getLastReadMessage()
                        .getId();

        return directMessageRepository
                .findByConversationOrderByCreatedAtAsc(
                        conversation
                )
                .stream()
                .filter(message ->
                        message.getId() > lastReadId
                )
                .count();

    }

}