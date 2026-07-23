package com.wave.backend.channelinvite.service;

import com.wave.backend.channelinvite.dto.ChannelInvitationResponse;
import com.wave.backend.channelinvite.dto.CreateChannelInvitationRequest;
import com.wave.backend.channelinvite.repository.ChannelInvitationRepository;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelInvitationService {

    private final ChannelInvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ChannelInvitationMapper mapper;
    private final ChannelInvitationCreateService createService;
    private final ChannelInvitationAcceptService acceptService;
    private final ChannelInvitationDeclineService declineService;

    public ChannelInvitationService(
            ChannelInvitationRepository invitationRepository,
            UserRepository userRepository,
            ChannelInvitationMapper mapper,
            ChannelInvitationCreateService createService,
            ChannelInvitationAcceptService acceptService,
            ChannelInvitationDeclineService declineService
    ) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.createService = createService;
        this.acceptService = acceptService;
        this.declineService = declineService;
    }

    public ChannelInvitationResponse createInvitation(
            CreateChannelInvitationRequest request
    ) {
        return createService.createInvitation(request);
    }

    public ChannelInvitationResponse acceptInvitation(
            Long invitationId
    ) {
        return acceptService.accept(invitationId);
    }

    public ChannelInvitationResponse declineInvitation(
            Long invitationId
    ) {
        return declineService.decline(invitationId);
    }

    @Transactional(readOnly = true)
    public List<ChannelInvitationResponse> getMyInvitations() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return invitationRepository
                .findByInviteeOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

}
