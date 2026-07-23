package com.wave.backend.user.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.dto.UserResponse;
import com.wave.backend.user.dto.UserSummaryResponse;
import com.wave.backend.user.dto.UpdateUserRequest;
import com.wave.backend.exception.UsernameAlreadyExistsException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        String username = request.getUsername().trim();
        userRepository.findByUsername(username)
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new UsernameAlreadyExistsException("Username already exists.");
                });

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setUsername(username);
        user.setBio(request.getBio() == null ? null : request.getBio().trim());
        user.setAvatar(request.getAvatar() == null ? null : request.getAvatar().trim());
        return toResponse(userRepository.save(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getAvatar(),
                user.getBio(),
                user.getPresencePreference()
        );
    }

    public List<UserSummaryResponse> searchUsers(
            String query
    ) {
        if (query == null || query.trim().length() < 2) {
            throw new IllegalArgumentException(
                    "Search query must contain at least 2 characters."
            );
        }

        String email = SecurityUtil.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return userRepository.searchSharedWorkspaceUsers(
                        currentUser,
                        query.trim()
                )
                .stream()
                .limit(20)
                .map(user -> new UserSummaryResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAvatar()
                ))
                .toList();
    }
}
