package com.tech_symfony.resource_server.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    
    @Value("${FRONTEND_URL:}")
    String FRONTEND_URL;

    public String getFRONTEND_URL() {
        if (StringUtils.isEmpty(FRONTEND_URL)) {
            FRONTEND_URL = "http://localhost:3000";
        }
        return FRONTEND_URL;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/specific", "/all");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // for browser
        registry.addEndpoint("/ws");
        registry.addEndpoint("/testws").setAllowedOrigins("http://localhost", getFRONTEND_URL()).withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000", getFRONTEND_URL()).withSockJS();
    }
}
