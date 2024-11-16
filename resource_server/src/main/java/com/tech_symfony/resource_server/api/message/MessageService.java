package com.tech_symfony.resource_server.api.message;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

public interface MessageService {
    void sendAll(Message message) throws MessagingException;
    void sendTo(Message message) throws MessagingException;
}

@Service
@RequiredArgsConstructor
class DefaultMessageService implements MessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendAll(Message message) throws MessagingException {
        simpMessagingTemplate.convertAndSend("/all/messages", message);
    }

    @Override
    public void sendTo(Message message) throws MessagingException {
        simpMessagingTemplate.convertAndSend("/specific/" + message.getTo() + "/messages", message);
    }
}