package com.wave.backend.emailverification.controller;

import com.wave.backend.emailverification.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email-verification")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(
            EmailVerificationService emailVerificationService
    ) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/verify")
    public String verifyEmail(
            @RequestParam String token
    ) {

        emailVerificationService.verifyEmail(token);

        return "Email verified successfully.";

    }

}