package com.wave.backend.reaction.controller;

import com.wave.backend.reaction.dto.CreateReactionRequest;
import com.wave.backend.reaction.dto.ReactionResponse;
import com.wave.backend.reaction.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(
            ReactionService reactionService
    ) {
        this.reactionService = reactionService;
    }

    @PostMapping
    public ReactionResponse react(

            @Valid
            @RequestBody
            CreateReactionRequest request

    ) {

        return reactionService.react(request);

    }

}