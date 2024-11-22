package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.message.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @MessageMapping("/notification/read")
    public Notification updateNotificationRead(@Payload int id) throws MessagingException {
        System.out.println("Update read notification: " + id);
        return notificationService.setAsRead(id);
    }
}