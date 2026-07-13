package com.wave.backend.directmessage.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.DirectMessageDeleteResponse;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageDeleteService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    public DirectMessageDeleteService(
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository
    ) {
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    public DirectMessageDeleteResponse deleteMessage(
            Long messageId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        DirectMessage message = directMessageRepository
                .findById(messageId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Direct message not found."
                        ));

        if (!message.getSender().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only delete your own messages."
            );

        }

        if (message.isDeleted()) {

            throw new IllegalArgumentException(
                    "Message has already been deleted."
            );

        }

        message.setDeleted(true);
        message.setContent("This message was deleted.");

        directMessageRepository.save(message);

        return new DirectMessageDeleteResponse(
                message.getId(),
                "Direct message deleted successfully."
        );

    }

}