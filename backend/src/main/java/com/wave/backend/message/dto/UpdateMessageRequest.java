package com.wave.backend.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMessageRequest {

    @NotBlank
    @Size(max = 4000)
    private String content;

}