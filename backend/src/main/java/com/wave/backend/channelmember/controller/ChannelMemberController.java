package com.wave.backend.channelmember.controller;

import com.wave.backend.channelmember.dto.ChannelMemberResponse;
import com.wave.backend.channelmember.service.ChannelMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channel-members")
public class ChannelMemberController {

    private final ChannelMemberService channelMemberService;

    public ChannelMemberController(
            ChannelMemberService channelMemberService
    ) {
        this.channelMemberService = channelMemberService;
    }

    @PostMapping("/{channelId}/join")
    public ChannelMemberResponse joinChannel(
            @PathVariable Long channelId
    ) {

        return channelMemberService.joinChannel(channelId);

    }

    @DeleteMapping("/{channelId}/leave")
    public void leaveChannel(
            @PathVariable Long channelId
    ) {

        channelMemberService.leaveChannel(channelId);

    }

    @GetMapping("/{channelId}")
    public List<ChannelMemberResponse> getMembers(
            @PathVariable Long channelId
    ) {

        return channelMemberService.getMembers(channelId);

    }

}