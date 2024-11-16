package com.tech_symfony.resource_server.api.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message send(Message message) throws Exception { return message; }

    @MessageMapping("/private")
    public void sendToSpecificUser(Message message) {
        simpMessagingTemplate.convertAndSend("/specific/" + message.getTo() + "/messages", message);
    }

}