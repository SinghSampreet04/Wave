package com.wave.backend.channel.controller;

import com.wave.backend.channel.dto.ChannelResponse;
import com.wave.backend.channel.dto.CreateChannelRequest;
import com.wave.backend.channel.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping
    public ChannelResponse createChannel(
            @Valid @RequestBody CreateChannelRequest request
    ) {
        return channelService.createChannel(request);
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<ChannelResponse> getWorkspaceChannels(
            @PathVariable Long workspaceId
    ) {
        return channelService.getWorkspaceChannels(workspaceId);
    }

    @DeleteMapping("/{channelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChannel(
            @PathVariable Long channelId
    ) {
        channelService.deleteChannel(channelId);
    }
}
