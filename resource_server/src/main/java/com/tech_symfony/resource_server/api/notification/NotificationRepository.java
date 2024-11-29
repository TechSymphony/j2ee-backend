package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findNotificationByUserOrderByCreatedAtDesc(User user);
}
