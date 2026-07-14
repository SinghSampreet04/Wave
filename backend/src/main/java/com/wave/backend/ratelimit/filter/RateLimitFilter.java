package com.wave.backend.ratelimit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wave.backend.ratelimit.config.RateLimitProperties;
import com.wave.backend.ratelimit.dto.RateLimitResponse;
import com.wave.backend.ratelimit.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitProperties properties;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(
            RateLimitService rateLimitService,
            RateLimitProperties properties,
            ObjectMapper objectMapper
    ) {
        this.rateLimitService = rateLimitService;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        String ip = request.getRemoteAddr();

        String key = null;
        int limit = 0;
        long window = 0;

        if (path.equals("/api/v1/auth/login")) {

            key = "login:" + ip;
            limit = properties.getLoginLimit();
            window = properties.getLoginWindowSeconds();

        } else if (path.equals("/api/v1/auth/register")) {

            key = "register:" + ip;
            limit = properties.getRegisterLimit();
            window = properties.getRegisterWindowSeconds();

        } else if (path.equals("/api/v1/password-reset/forgot")) {

            key = "forgot:" + ip;
            limit = properties.getForgotPasswordLimit();
            window = properties.getForgotPasswordWindowSeconds();

        } else if (path.equals("/api/v1/email-verification/resend")) {

            key = "verify:" + ip;
            limit = properties.getResendVerificationLimit();
            window = properties.getResendVerificationWindowSeconds();

        }

        if (key != null) {

            boolean allowed =
                    rateLimitService.isAllowed(
                            key,
                            limit,
                            window
                    );

            if (!allowed) {

                long retryAfter =
                        rateLimitService.getRemainingTime(key);

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                response.setHeader(
                        "Retry-After",
                        String.valueOf(retryAfter)
                );

                RateLimitResponse body =
                        new RateLimitResponse(
                                429,
                                "Too Many Requests",
                                "Too many requests. Please try again later.",
                                LocalDateTime.now()
                        );

                objectMapper.writeValue(
                        response.getOutputStream(),
                        body
                );

                return;
            }

        }

        filterChain.doFilter(request, response);

    }

}