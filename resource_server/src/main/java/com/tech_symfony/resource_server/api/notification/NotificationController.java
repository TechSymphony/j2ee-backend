package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.message.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @MessageMapping("/application")
    public void send(Notification notification) throws MessagingException {
        notificationService.sendAll(notification);
    }

    @MessageMapping("/private")
    public void sendToSpecificUser(Notification notification) throws MessagingException {
        notificationService.sendTo(notification);
    }

}