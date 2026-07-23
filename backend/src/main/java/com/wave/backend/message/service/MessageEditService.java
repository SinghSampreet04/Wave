package com.wave.backend.message.service;

import com.wave.backend.common.event.MessageEditedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageAccessDeniedException;
import com.wave.backend.exception.MessageAlreadyDeletedException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.dto.MessageResponse;
import com.wave.backend.message.dto.UpdateMessageRequest;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.mention.service.MentionService;
import com.wave.backend.reaction.dto.ReactionResponse;
import com.wave.backend.reaction.entity.Reaction;
import com.wave.backend.reaction.repository.ReactionRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageEditService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MentionService mentionService;

    public MessageEditService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ReactionRepository reactionRepository,
            ApplicationEventPublisher eventPublisher,
            MentionService mentionService
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
        this.eventPublisher = eventPublisher;
        this.mentionService = mentionService;
    }

    @Transactional
    public MessageResponse editMessage(
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
            throw new MessageAccessDeniedException(
                    "You can only edit your own messages."
            );
        }

        if (message.isDeleted()) {
            throw new MessageAlreadyDeletedException(
                    "Deleted messages cannot be edited."
            );
        }

        message.setContent(request.getContent());
        message.setEdited(true);
        message.setEditedAt(LocalDateTime.now());

        message = messageRepository.save(message);
        mentionService.processChannelMentions(message);

        final Long savedMessageId = message.getId();

        eventPublisher.publishEvent(
                new MessageEditedEvent(
                        savedMessageId,
                        message.getChannel().getId(),
                        message.getContent(),
                        message.getEditedAt()
                )
        );

        List<Reaction> reactions =
                reactionRepository.findAllByMessageOrderByEmojiAsc(message);

        Map<String, ReactionResponse> grouped = new LinkedHashMap<>();

        for (Reaction reaction : reactions) {

            ReactionResponse response = grouped.computeIfAbsent(
                    reaction.getEmoji(),
                    emoji -> new ReactionResponse(
                            savedMessageId,
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
                savedMessageId,
                currentUser.getId(),
                currentUser.getUsername(),
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
