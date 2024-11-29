package com.tech_symfony.resource_server.api.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping("/{username}")
    public List<Notification> getNotificationsByUserName(@PathVariable("username") String username) {
        return notificationService.getNotificationsByUserName(username);
    }

    @PutMapping("/read/{id}")
    public Notification updateNotificationRead(@PathVariable("id") int id) {
        return notificationService.setAsRead(id);
    }
}
