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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

        validateParticipant(conversation, currentUser);
        if (!message.getConversation().getId().equals(conversation.getId())) {
            throw new IllegalArgumentException(
                    "Message does not belong to this conversation."
            );
        }

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

    @Transactional(readOnly = true)
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

        validateParticipant(conversation, currentUser);
        return getUnreadCount(conversation, currentUser);
    }

    public long getUnreadCount(
            Conversation conversation,
            User currentUser
    ) {
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
                    .stream()
                    .filter(message ->
                            !message.getSender().getId().equals(currentUser.getId())
                    )
                    .count();

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
                                && !message.getSender().getId()
                                .equals(currentUser.getId())
                )
                .count();

    }

    private void validateParticipant(
            Conversation conversation,
            User currentUser
    ) {
        if (!conversation.getUserOne().getId().equals(currentUser.getId())
                && !conversation.getUserTwo().getId()
                .equals(currentUser.getId())) {
            throw new IllegalArgumentException(
                    "You are not part of this conversation."
            );
        }
    }

}
