package com.wave.backend.websocket.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.PresenceStatus;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.websocket.dto.PresenceMessage;
import com.wave.backend.websocket.session.SessionInfo;
import com.wave.backend.websocket.session.SessionRegistry;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class PresenceService {

    private static final long LEASE_SECONDS = 90;

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistry sessionRegistry;

    public PresenceService(
            StringRedisTemplate redisTemplate,
            UserRepository userRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            SimpMessagingTemplate messagingTemplate,
            SessionRegistry sessionRegistry
    ) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistry = sessionRegistry;
    }

    @Transactional(readOnly = true)
    public void userConnected(User user, String sessionId) {
        renewLease(user.getId(), sessionId);
        broadcast(user);
    }

    @Transactional(readOnly = true)
    public void userDisconnected(User user, String sessionId) {
        redisTemplate.opsForZSet().remove(key(user.getId()), sessionId);
        broadcast(user);
    }

    @Transactional
    public PresenceMessage updateMyStatus(PresenceStatus status) {
        if (status == PresenceStatus.OFFLINE) {
            throw new IllegalArgumentException(
                    "Offline is derived from connection state. Use Invisible instead."
            );
        }

        User user = currentUser();
        user.setPresencePreference(status);
        userRepository.save(user);
        broadcast(user);
        return responseFor(user, true);
    }

    @Transactional(readOnly = true)
    public List<PresenceMessage> getWorkspacePresence(Long workspaceId) {
        User current = currentUser();
        List<com.wave.backend.workspace.entity.WorkspaceMember> memberships =
                workspaceMemberRepository.findByWorkspace_Id(workspaceId);

        if (memberships.isEmpty() || memberships.stream()
                .noneMatch(member -> member.getUser().getId().equals(current.getId()))) {
            throw new IllegalArgumentException("Workspace access denied.");
        }

        return memberships.stream()
                .map(member -> responseFor(
                        member.getUser(),
                        member.getUser().getId().equals(current.getId())
                ))
                .toList();
    }

    @Scheduled(fixedDelay = 30_000)
    public void renewActiveLeases() {
        for (SessionInfo session : sessionRegistry.getSessions()) {
            renewLease(session.getUserId(), session.getSessionId());
        }
    }

    private void renewLease(Long userId, String sessionId) {
        redisTemplate.opsForZSet().add(
                key(userId),
                sessionId,
                Instant.now().getEpochSecond()
        );
    }

    private boolean isOnline(Long userId) {
        String key = key(userId);
        redisTemplate.opsForZSet().removeRangeByScore(
                key,
                0,
                Instant.now().minusSeconds(LEASE_SECONDS).getEpochSecond()
        );
        Long count = redisTemplate.opsForZSet().zCard(key);
        return count != null && count > 0;
    }

    private PresenceMessage responseFor(User user, boolean self) {
        PresenceStatus status = isOnline(user.getId())
                ? user.getPresencePreference()
                : PresenceStatus.OFFLINE;
        if (!self && status == PresenceStatus.INVISIBLE) {
            status = PresenceStatus.OFFLINE;
        }
        return new PresenceMessage(user.getId(), user.getUsername(), status);
    }

    private void broadcast(User user) {
        PresenceMessage publicResponse = responseFor(user, false);
        Set<Long> workspaceIds = workspaceMemberRepository.findByUser(user)
                .stream()
                .map(member -> member.getWorkspace().getId())
                .collect(java.util.stream.Collectors.toSet());
        workspaceIds.forEach(workspaceId ->
                messagingTemplate.convertAndSend(
                        "/topic/workspaces/" + workspaceId + "/presence",
                        publicResponse
                )
        );
    }

    private User currentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    private String key(Long userId) {
        return "wave:presence:user:" + userId;
    }
}
