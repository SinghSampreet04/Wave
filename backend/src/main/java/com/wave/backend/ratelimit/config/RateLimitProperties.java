package com.wave.backend.ratelimit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private int loginLimit;
    private int registerLimit;
    private int forgotPasswordLimit;
    private int resendVerificationLimit;

    private long loginWindowSeconds;
    private long registerWindowSeconds;
    private long forgotPasswordWindowSeconds;
    private long resendVerificationWindowSeconds;

    public RateLimitProperties() {
    }

    public int getLoginLimit() {
        return loginLimit;
    }

    public void setLoginLimit(int loginLimit) {
        this.loginLimit = loginLimit;
    }

    public int getRegisterLimit() {
        return registerLimit;
    }

    public void setRegisterLimit(int registerLimit) {
        this.registerLimit = registerLimit;
    }

    public int getForgotPasswordLimit() {
        return forgotPasswordLimit;
    }

    public void setForgotPasswordLimit(int forgotPasswordLimit) {
        this.forgotPasswordLimit = forgotPasswordLimit;
    }

    public int getResendVerificationLimit() {
        return resendVerificationLimit;
    }

    public void setResendVerificationLimit(int resendVerificationLimit) {
        this.resendVerificationLimit = resendVerificationLimit;
    }

    public long getLoginWindowSeconds() {
        return loginWindowSeconds;
    }

    public void setLoginWindowSeconds(long loginWindowSeconds) {
        this.loginWindowSeconds = loginWindowSeconds;
    }

    public long getRegisterWindowSeconds() {
        return registerWindowSeconds;
    }

    public void setRegisterWindowSeconds(long registerWindowSeconds) {
        this.registerWindowSeconds = registerWindowSeconds;
    }

    public long getForgotPasswordWindowSeconds() {
        return forgotPasswordWindowSeconds;
    }

    public void setForgotPasswordWindowSeconds(long forgotPasswordWindowSeconds) {
        this.forgotPasswordWindowSeconds = forgotPasswordWindowSeconds;
    }

    public long getResendVerificationWindowSeconds() {
        return resendVerificationWindowSeconds;
    }

    public void setResendVerificationWindowSeconds(long resendVerificationWindowSeconds) {
        this.resendVerificationWindowSeconds = resendVerificationWindowSeconds;
    }
}