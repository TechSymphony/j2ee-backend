package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

public interface NotificationService {
    void sendAll(Notification notification) throws MessagingException;
    void sendTo(Notification notification) throws MessagingException;
}

@Service
@RequiredArgsConstructor
class DefaultMessageService implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendAll(Notification notification) throws MessagingException {
        // TODO: send all later (user id may be null)
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/all/messages", notification);
    }

    @Override
    public void sendTo(Notification notification) throws MessagingException {
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/specific/" + notification.getUser().getId() + "/messages", notification);
    }
}