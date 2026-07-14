package com.wave.backend.email.controller;

import com.wave.backend.email.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/api/v1/email/test")
    public String sendTestEmail() throws MessagingException {

        emailService.sendHtmlEmail(
                "brarsam2004@gmail.com",
                "Wave Email Test",
                """
                <html>
                    <body style="font-family:Arial,sans-serif">
                        <h1>🎉 Wave Email Works!</h1>

                        <p>Your Spring Boot application successfully sent this email.</p>

                        <p>If you're reading this, your SMTP configuration is working correctly.</p>

                        <hr>

                        <p><strong>Wave Backend</strong></p>
                    </body>
                </html>
                """
        );

        return "Email sent successfully!";
    }

}