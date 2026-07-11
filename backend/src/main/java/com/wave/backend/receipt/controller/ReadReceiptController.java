package com.wave.backend.receipt.controller;

import com.wave.backend.receipt.dto.CreateReadReceiptRequest;
import com.wave.backend.receipt.dto.ReadReceiptResponse;
import com.wave.backend.receipt.service.ReadReceiptService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/read-receipts")
public class ReadReceiptController {

    private final ReadReceiptService readReceiptService;

    public ReadReceiptController(
            ReadReceiptService readReceiptService
    ) {
        this.readReceiptService = readReceiptService;
    }

    @PostMapping
    public ReadReceiptResponse markAsRead(
            @Valid @RequestBody CreateReadReceiptRequest request
    ) {

        return readReceiptService.markAsRead(request);

    }

}