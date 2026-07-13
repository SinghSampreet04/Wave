package com.wave.backend.channelinvite.controller;

import com.wave.backend.channelinvite.dto.ChannelInvitationResponse;
import com.wave.backend.channelinvite.dto.CreateChannelInvitationRequest;
import com.wave.backend.channelinvite.service.ChannelInvitationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channel-invitations")
public class ChannelInvitationController {

    private final ChannelInvitationService channelInvitationService;

    public ChannelInvitationController(
            ChannelInvitationService channelInvitationService
    ) {
        this.channelInvitationService = channelInvitationService;
    }

    @PostMapping
    public ChannelInvitationResponse createInvitation(
            @Valid @RequestBody CreateChannelInvitationRequest request
    ) {

        return channelInvitationService.createInvitation(
                request
        );

    }

    @PostMapping("/{invitationId}/accept")
    public ChannelInvitationResponse acceptInvitation(
            @PathVariable Long invitationId
    ) {

        return channelInvitationService.acceptInvitation(
                invitationId
        );

    }

    @PostMapping("/{invitationId}/decline")
    public ChannelInvitationResponse declineInvitation(
            @PathVariable Long invitationId
    ) {

        return channelInvitationService.declineInvitation(
                invitationId
        );

    }

    @GetMapping("/me")
    public List<ChannelInvitationResponse> myInvitations() {

        return channelInvitationService.getMyInvitations();

    }

}