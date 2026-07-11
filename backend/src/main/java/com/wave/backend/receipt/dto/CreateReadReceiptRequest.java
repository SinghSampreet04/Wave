package com.wave.backend.receipt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReadReceiptRequest {

    @NotNull
    private Long messageId;

}