package com.wave.backend.mention.service;

import com.wave.backend.common.event.MentionCreatedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.entity.Message;
import com.wave.backend.mention.dto.MentionResponse;
import com.wave.backend.mention.entity.Mention;
import com.wave.backend.mention.repository.MentionRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MentionService {

    private static final Pattern MENTION_PATTERN =
            Pattern.compile("@([A-Za-z0-9_.-]+)");

    private final MentionRepository mentionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MentionService(
            MentionRepository mentionRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.mentionRepository = mentionRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public void processChannelMentions(
            Message message
    ) {

        processMentions(
                message.getContent(),
                message.getSender(),
                message,
                null
        );

    }

    public void processDirectMentions(
            DirectMessage directMessage
    ) {

        processMentions(
                directMessage.getContent(),
                directMessage.getSender(),
                null,
                directMessage
        );

    }

    public List<MentionResponse> getMyMentions() {

        User currentUser = getCurrentUser();

        return mentionRepository
                .findByMentionedUserOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public List<MentionResponse> getUnreadMentions() {

        User currentUser = getCurrentUser();

        return mentionRepository
                .findByMentionedUserAndReadFalseOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public long getUnreadMentionCount() {

        User currentUser = getCurrentUser();

        return mentionRepository
                .countByMentionedUserAndReadFalse(
                        currentUser
                );

    }

    public MentionResponse markAsRead(
            Long mentionId
    ) {

        User currentUser = getCurrentUser();

        Mention mention = mentionRepository
                .findByIdAndMentionedUser(
                        mentionId,
                        currentUser
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Mention not found."
                        ));

        if (!mention.isRead()) {

            mention.setRead(true);
            mention.setReadAt(LocalDateTime.now());

            mention = mentionRepository.save(mention);

        }

        return toResponse(mention);

    }

    private void processMentions(
            String content,
            User sender,
            Message message,
            DirectMessage directMessage
    ) {

        Matcher matcher =
                MENTION_PATTERN.matcher(content);

        Set<String> usernames =
                new HashSet<>();

        while (matcher.find()) {

            usernames.add(
                    matcher.group(1)
            );

        }

        for (String username : usernames) {

            User mentionedUser =
                    userRepository.findByUsername(username)
                            .orElse(null);

            if (mentionedUser == null) {
                continue;
            }

            if (mentionedUser.getId().equals(sender.getId())) {
                continue;
            }

            Mention mention =
                    new Mention();

            mention.setMentionedBy(sender);
            mention.setMentionedUser(mentionedUser);
            mention.setMessage(message);
            mention.setDirectMessage(directMessage);

            mention = mentionRepository.save(mention);

            eventPublisher.publishEvent(
                    new MentionCreatedEvent(
                            mention.getId(),
                            mentionedUser.getId(),
                            sender.getId(),
                            sender.getUsername(),
                            message != null
                                    ? message.getId()
                                    : null,
                            directMessage != null
                                    ? directMessage.getId()
                                    : null
                    )
            );

        }

    }

    private User getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

    }

    private MentionResponse toResponse(
            Mention mention
    ) {

        return new MentionResponse(
                mention.getId(),
                mention.getMentionedBy().getId(),
                mention.getMentionedBy().getUsername(),
                mention.getMessage() != null
                        ? mention.getMessage().getId()
                        : null,
                mention.getDirectMessage() != null
                        ? mention.getDirectMessage().getId()
                        : null,
                mention.isRead(),
                mention.getCreatedAt()
        );

    }

}