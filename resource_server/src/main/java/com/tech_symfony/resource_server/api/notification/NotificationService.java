package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.message.Message;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NotificationService {
    void sendAll(Notification notification) throws MessagingException;
    void sendTo(Notification notification) throws MessagingException;
    void sendMessagesToUser(User user);
    Notification setAsRead(int notificationId);
    List<Notification> getNotificationsByUserName(String userName);
}

@Service
@RequiredArgsConstructor
class DefaultMessageService implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<Notification> getNotificationsByUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return notificationRepository.findNotificationByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public void sendAll(Notification notification) throws MessagingException {
        // TODO: send all later (user id may be null)
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/all/messages", notification);
    }

    @Override
    public void sendTo(Notification notification) throws MessagingException {
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSend("/specific/" + notification.getUser().getUsername() + "/messages", notification);
    }

    @Override
    public void sendMessagesToUser(User user) {
        List<Notification> notifications = notificationRepository.findNotificationByUserOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            simpMessagingTemplate.convertAndSend("/specific/" + user.getUsername() + "/messages", notification);
        }
    }

    @Override
    public Notification setAsRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}