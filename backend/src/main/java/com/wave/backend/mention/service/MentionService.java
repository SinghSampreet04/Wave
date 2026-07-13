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

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

        return mentionRepository
                .findByMentionedUserOrderByCreatedAtDesc(
                        currentUser
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public long getUnreadMentionCount() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

        return mentionRepository.countByMentionedUserAndReadFalse(
                currentUser
        );

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
                            message != null ? message.getId() : null,
                            directMessage != null ? directMessage.getId() : null
                    )
            );

        }

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