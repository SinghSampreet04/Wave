package com.wave.backend.directreaction.controller;

import com.wave.backend.directreaction.dto.CreateDirectReactionRequest;
import com.wave.backend.directreaction.dto.DirectReactionResponse;
import com.wave.backend.directreaction.service.DirectReactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/direct-reactions")
public class DirectReactionController {

    private final DirectReactionService directReactionService;

    public DirectReactionController(
            DirectReactionService directReactionService
    ) {
        this.directReactionService = directReactionService;
    }

    @PostMapping
    public DirectReactionResponse react(
            @Valid @RequestBody CreateDirectReactionRequest request
    ) {

        return directReactionService.react(
                request
        );

    }

}