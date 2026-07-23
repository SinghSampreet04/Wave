package com.wave.backend.websocket.interceptor;

import com.wave.backend.auth.service.CustomUserDetailsService;
import com.wave.backend.websocket.service.WebSocketAccessService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final CustomUserDetailsService customUserDetailsService;
    private final WebSocketAccessService webSocketAccessService;

    public JwtChannelInterceptor(
            CustomUserDetailsService customUserDetailsService,
            WebSocketAccessService webSocketAccessService
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.webSocketAccessService = webSocketAccessService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String username = null;

        if (accessor.getSessionAttributes() != null) {
            username = (String) accessor.getSessionAttributes().get("username");
        }

        boolean authenticationAdded = false;

        if (username != null && accessor.getUser() == null) {

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            accessor.setUser(authentication);
            accessor.setLeaveMutable(true);
            authenticationAdded = true;
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            if (username == null || accessor.getDestination() == null) {
                throw new org.springframework.messaging.MessageDeliveryException(
                        "Authenticated subscription required."
                );
            }

            webSocketAccessService.validateSubscription(
                    username,
                    accessor.getDestination()
            );
        }

        if (authenticationAdded) {
            return MessageBuilder.createMessage(
                    message.getPayload(),
                    accessor.getMessageHeaders()
            );
        }

        return message;
    }

}
