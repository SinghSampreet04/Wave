package com.wave.backend.reaction.service;

import com.wave.backend.channelmember.service.ChannelMemberService;
import com.wave.backend.common.event.ReactionAddedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.MessageAlreadyDeletedException;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.reaction.dto.CreateReactionRequest;
import com.wave.backend.reaction.dto.ReactionResponse;
import com.wave.backend.reaction.entity.Reaction;
import com.wave.backend.reaction.repository.ReactionRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class ReactionService {

    private static final Set<String> SUPPORTED_EMOJIS = Set.of(
            "😀", "😂", "❤️", "👍", "🎉", "🔥",
            "👏", "😮", "😢", "🤔", "👀", "🚀"
    );

    private final ReactionRepository reactionRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMemberService channelMemberService;
    private final ApplicationEventPublisher eventPublisher;

    public ReactionService(
            ReactionRepository reactionRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelMemberService channelMemberService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.reactionRepository = reactionRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelMemberService = channelMemberService;
        this.eventPublisher = eventPublisher;
    }

    public ReactionResponse react(CreateReactionRequest request) {
        if (!SUPPORTED_EMOJIS.contains(request.getEmoji())) {
            throw new IllegalArgumentException("Unsupported reaction.");
        }

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Message message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        if (message.isDeleted()) {
            throw new MessageAlreadyDeletedException(
                    "Deleted messages cannot receive reactions."
            );
        }

        // Enforce private channel access
        channelMemberService.validateChannelAccess(
                message.getChannel(),
                user
        );

        Reaction existingReaction = reactionRepository
                .findByMessageAndUserAndEmoji(
                        message,
                        user,
                        request.getEmoji()
                )
                .orElse(null);

        boolean reacted;

        if (existingReaction != null) {

            reactionRepository.delete(existingReaction);
            reacted = false;

        } else {

            Reaction reaction = new Reaction();
            reaction.setMessage(message);
            reaction.setUser(user);
            reaction.setEmoji(request.getEmoji());

            reactionRepository.save(reaction);

            reacted = true;

        }

        long count = reactionRepository.countByMessageAndEmoji(
                message,
                request.getEmoji()
        );

        eventPublisher.publishEvent(
                new ReactionAddedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        request.getEmoji(),
                        count,
                        user.getId(),
                        reacted
                )
        );

        return new ReactionResponse(
                message.getId(),
                request.getEmoji(),
                count,
                reacted
        );

    }

}
