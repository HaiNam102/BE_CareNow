package com.example.Cap2.NannyNow.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Change from setAllowedOrigins to setAllowedOriginPatterns
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000", "http://192.168.1.4:3000");
        
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000", "http://192.168.1.4:3000")
            .withSockJS();
    }

}
