package com.tech_symfony.resource_server.api.message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/application")
    public void send(Message message) throws MessagingException {
        messageService.sendAll(message);
    }

    @MessageMapping("/private")
    public void sendToSpecificUser(Message message) throws MessagingException {
        messageService.sendTo(message);
    }

}
