package com.wave.backend.directreaction.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.directreaction.dto.CreateDirectReactionRequest;
import com.wave.backend.directreaction.dto.DirectReactionResponse;
import com.wave.backend.directreaction.entity.DirectReaction;
import com.wave.backend.directreaction.repository.DirectReactionRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.wave.backend.directreaction.dto.DirectReactionEventResponse;

@Service
@Transactional
public class DirectReactionService {

    private final DirectReactionRepository directReactionRepository;
    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public DirectReactionService(
            DirectReactionRepository directReactionRepository,
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.directReactionRepository = directReactionRepository;
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public DirectReactionResponse react(
            CreateDirectReactionRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        DirectMessage directMessage = directMessageRepository
                .findById(request.getDirectMessageId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Direct message not found."
                        ));

        Conversation conversation =
                directMessage.getConversation();

        boolean isParticipant =
                conversation.getUserOne().getId().equals(currentUser.getId())
                        || conversation.getUserTwo().getId().equals(currentUser.getId());

        if (!isParticipant) {

            throw new IllegalArgumentException(
                    "You are not a participant in this conversation."
            );

        }

        DirectReaction existingReaction =
                directReactionRepository
                        .findByDirectMessageAndUserAndEmoji(
                                directMessage,
                                currentUser,
                                request.getEmoji()
                        );

        boolean reacted;

        if (existingReaction != null) {

            directReactionRepository.delete(existingReaction);

            reacted = false;

        } else {

            DirectReaction reaction = new DirectReaction();

            reaction.setDirectMessage(directMessage);
            reaction.setUser(currentUser);
            reaction.setEmoji(request.getEmoji());

            directReactionRepository.save(reaction);

            reacted = true;

        }

        long count =
                directReactionRepository
                        .countByDirectMessageAndEmoji(
                                directMessage,
                                request.getEmoji()
                        );

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversation.getId() + "/reactions",
                new DirectReactionEventResponse(
                        directMessage.getId(),
                        request.getEmoji(),
                        count,
                        currentUser.getId(),
                        reacted
                )
        );

        return new DirectReactionResponse(
                directMessage.getId(),
                request.getEmoji(),
                count,
                reacted
        );

    }

}
