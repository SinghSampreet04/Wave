package com.wave.backend.websocket.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.service.ChannelMemberService;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WebSocketAccessService {

    private static final Pattern CHANNEL_TOPIC =
            Pattern.compile("^/topic/channels/(\\d+)(?:/.*)?$");
    private static final Pattern THREAD_TOPIC =
            Pattern.compile("^/topic/threads/(\\d+)$");
    private static final Pattern CONVERSATION_TOPIC =
            Pattern.compile("^/topic/conversations/(\\d+)(?:/.*)?$");
    private static final Pattern NOTIFICATION_TOPIC =
            Pattern.compile("^/topic/notifications/(\\d+)$");
    private static final Pattern USER_TOPIC =
            Pattern.compile("^/topic/users/(\\d+)(?:/.*)?$");
    private static final Pattern WORKSPACE_TOPIC =
            Pattern.compile("^/topic/workspaces/(\\d+)(?:/.*)?$");

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ChannelMemberService channelMemberService;

    public WebSocketAccessService(
            UserRepository userRepository,
            ChannelRepository channelRepository,
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            ChannelMemberService channelMemberService
    ) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.channelMemberService = channelMemberService;
    }

    public void validateSubscription(
            String email,
            String destination
    ) {
        User user = getUser(email);

        Matcher channelMatcher = CHANNEL_TOPIC.matcher(destination);
        if (channelMatcher.matches()) {
            validateChannel(
                    Long.parseLong(channelMatcher.group(1)),
                    user
            );
            return;
        }

        Matcher threadMatcher = THREAD_TOPIC.matcher(destination);
        if (threadMatcher.matches()) {
            Message parent = messageRepository.findById(
                            Long.parseLong(threadMatcher.group(1))
                    )
                    .orElseThrow(() ->
                            new MessageNotFoundException("Message not found."));
            channelMemberService.validateChannelAccess(
                    parent.getChannel(),
                    user
            );
            return;
        }

        Matcher conversationMatcher =
                CONVERSATION_TOPIC.matcher(destination);
        if (conversationMatcher.matches()) {
            validateConversation(
                    Long.parseLong(conversationMatcher.group(1)),
                    user
            );
            return;
        }

        Matcher notificationMatcher =
                NOTIFICATION_TOPIC.matcher(destination);
        if (notificationMatcher.matches()) {
            validateUserTopic(notificationMatcher, user);
            return;
        }

        Matcher userMatcher = USER_TOPIC.matcher(destination);
        if (userMatcher.matches()) {
            validateUserTopic(userMatcher, user);
            return;
        }

        Matcher workspaceMatcher = WORKSPACE_TOPIC.matcher(destination);
        if (workspaceMatcher.matches()) {
            Long workspaceId = Long.parseLong(workspaceMatcher.group(1));
            boolean allowed = channelMemberService
                    .isWorkspaceMember(workspaceId, user);
            if (!allowed) {
                throw new MessageDeliveryException("Workspace access denied.");
            }
            return;
        }

        throw new MessageDeliveryException(
                "Subscription destination is not allowed."
        );
    }

    public void validateChannel(
            Long channelId,
            User user
    ) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));
        channelMemberService.validateChannelAccess(channel, user);
    }

    public void validateConversation(
            Long conversationId,
            User user
    ) {
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new MessageDeliveryException(
                                "Conversation not found."
                        ));

        if (!conversation.getUserOne().getId().equals(user.getId())
                && !conversation.getUserTwo().getId().equals(user.getId())) {
            throw new MessageDeliveryException(
                    "Conversation access denied."
            );
        }
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }

    private void validateUserTopic(
            Matcher matcher,
            User user
    ) {
        Long requestedUserId = Long.parseLong(matcher.group(1));
        if (!requestedUserId.equals(user.getId())) {
            throw new MessageDeliveryException(
                    "User topic access denied."
            );
        }
    }
}
