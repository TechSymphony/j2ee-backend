//package com.tech_symfony.resource_server.system.interceptor;
//
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//
//public class AuthChannelInterceptor implements ChannelInterceptor {
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        if (message.getHeaders().get(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER) != null) {
//            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//            String token = accessor.getFirstNativeHeader("Authorization");
//
//            // Validate the token (replace with your logic)
//            if (token != null && validateToken(token)) {
//                // Optionally set user details in the session
//                accessor.setUser();
//            } else {
//                throw new IllegalArgumentException("Invalid token");
//            }
//        }
//        return message;
//    }
//
//    private boolean validateToken(String token) {
//        // Implement your token validation logic here
//        return true; // Replace with actual validation
//
//    }
