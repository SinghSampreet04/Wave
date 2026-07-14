package com.wave.backend.passwordreset.controller;

import com.wave.backend.passwordreset.dto.request.ForgotPasswordRequest;
import com.wave.backend.passwordreset.dto.request.ResetPasswordRequest;
import com.wave.backend.passwordreset.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService
    ) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot")
    public String forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) throws MessagingException {

        passwordResetService.forgotPassword(request.getEmail());

        return "Password reset email sent.";
    }

    @PostMapping("/reset")
    public String resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        passwordResetService.resetPassword(request);

        return "Password updated successfully.";
    }

}