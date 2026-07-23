package com.wave.backend.directmessage.service;

import com.wave.backend.common.event.DirectMessageSentEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.DirectMessageResponse;
import com.wave.backend.directmessage.dto.DirectMessageContextResponse;
import com.wave.backend.directmessage.dto.SendDirectMessageRequest;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.mention.service.MentionService;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import com.wave.backend.directreaction.repository.DirectReactionRepository;
import com.wave.backend.directreaction.dto.DirectReactionResponse;
import com.wave.backend.directreaction.entity.DirectReaction;

@Service
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MentionService mentionService;
    private final MetricsService metricsService;
    private final DirectReactionRepository directReactionRepository;

    public DirectMessageService(
            DirectMessageRepository directMessageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher,
            MentionService mentionService,
            MetricsService metricsService,
            DirectReactionRepository directReactionRepository
    ) {
        this.directMessageRepository = directMessageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.mentionService = mentionService;
        this.metricsService = metricsService;
        this.directReactionRepository = directReactionRepository;
    }

    @Transactional
    public DirectMessageResponse sendMessage(
            SendDirectMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        return sendMessage(request, email);
    }

    @Transactional
    public DirectMessageResponse sendMessage(
            SendDirectMessageRequest request,
            String email
    ) {

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

        metricsService.incrementMessageSent();

        conversation.setLastMessage(message.getContent());
        conversation.setLastMessageAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        mentionService.processDirectMentions(message);

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

        return toResponse(message, sender);

    }

    @Transactional(readOnly = true)
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
                .map(message -> toResponse(message, currentUser))
                .toList();

    }

    @Transactional(readOnly = true)
    public DirectMessageContextResponse getMessageContext(Long messageId) {
        String email = SecurityUtil.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
        DirectMessage message = directMessageRepository.findById(messageId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Direct message not found."
                        ));
        Conversation conversation = message.getConversation();
        if (!conversation.getUserOne().getId().equals(currentUser.getId())
                && !conversation.getUserTwo().getId()
                .equals(currentUser.getId())) {
            throw new IllegalArgumentException(
                    "You are not part of this conversation."
            );
        }
        return new DirectMessageContextResponse(
                message.getId(),
                conversation.getId()
        );
    }

    private DirectMessageResponse toResponse(
            DirectMessage message,
            User currentUser
    ) {
        Map<String, DirectReactionResponse> grouped = new LinkedHashMap<>();
        for (DirectReaction reaction :
                directReactionRepository.findAllByDirectMessageOrderByEmojiAsc(message)) {
            DirectReactionResponse response = grouped.computeIfAbsent(
                    reaction.getEmoji(),
                    emoji -> new DirectReactionResponse(message.getId(), emoji, 0, false)
            );
            response.setCount(response.getCount() + 1);
            if (reaction.getUser().getId().equals(currentUser.getId())) {
                response.setReacted(true);
            }
        }

        return new DirectMessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.isEdited(),
                message.isDeleted(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                List.copyOf(grouped.values())
        );

    }

}
