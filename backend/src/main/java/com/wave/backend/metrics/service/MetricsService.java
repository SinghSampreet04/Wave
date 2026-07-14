package com.wave.backend.metrics.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;

    private final Counter registrationCounter;

    private final Counter passwordResetRequestCounter;
    private final Counter passwordResetSuccessCounter;

    private final Counter emailVerificationSentCounter;
    private final Counter emailVerificationSuccessCounter;

    private final Counter fileUploadCounter;

    private final Counter workspaceCreatedCounter;

    private final Counter channelCreatedCounter;

    private final Counter messageSentCounter;

    public MetricsService(MeterRegistry meterRegistry) {

        loginSuccessCounter =
                Counter.builder("wave.login.success")
                        .description("Successful logins")
                        .register(meterRegistry);

        loginFailureCounter =
                Counter.builder("wave.login.failure")
                        .description("Failed logins")
                        .register(meterRegistry);

        registrationCounter =
                Counter.builder("wave.registration.success")
                        .description("Registered users")
                        .register(meterRegistry);

        passwordResetRequestCounter =
                Counter.builder("wave.password.reset.request")
                        .description("Password reset requests")
                        .register(meterRegistry);

        passwordResetSuccessCounter =
                Counter.builder("wave.password.reset.success")
                        .description("Successful password resets")
                        .register(meterRegistry);

        emailVerificationSentCounter =
                Counter.builder("wave.email.verification.sent")
                        .description("Verification emails sent")
                        .register(meterRegistry);

        emailVerificationSuccessCounter =
                Counter.builder("wave.email.verification.success")
                        .description("Verified email addresses")
                        .register(meterRegistry);

        fileUploadCounter =
                Counter.builder("wave.file.upload")
                        .description("Uploaded files")
                        .register(meterRegistry);

        workspaceCreatedCounter =
                Counter.builder("wave.workspace.created")
                        .description("Created workspaces")
                        .register(meterRegistry);

        channelCreatedCounter =
                Counter.builder("wave.channel.created")
                        .description("Created channels")
                        .register(meterRegistry);

        messageSentCounter =
                Counter.builder("wave.message.sent")
                        .description("Messages sent")
                        .register(meterRegistry);
    }

    // ============================================================
    // Existing methods (keep compatibility with current services)
    // ============================================================

    public void incrementSuccessfulLogins() {
        loginSuccessCounter.increment();
    }

    public void incrementFailedLogins() {
        loginFailureCounter.increment();
    }

    public void incrementUsersRegistered() {
        registrationCounter.increment();
    }

    public void incrementPasswordResetSent() {
        passwordResetRequestCounter.increment();
    }

    // ============================================================
    // New metrics
    // ============================================================

    public void incrementPasswordResetSuccess() {
        passwordResetSuccessCounter.increment();
    }

    public void incrementEmailVerificationSent() {
        emailVerificationSentCounter.increment();
    }

    public void incrementEmailVerificationSuccess() {
        emailVerificationSuccessCounter.increment();
    }

    public void incrementFileUpload() {
        fileUploadCounter.increment();
    }

    public void incrementWorkspaceCreated() {
        workspaceCreatedCounter.increment();
    }

    public void incrementChannelCreated() {
        channelCreatedCounter.increment();
    }

    public void incrementMessageSent() {
        messageSentCounter.increment();
    }
}