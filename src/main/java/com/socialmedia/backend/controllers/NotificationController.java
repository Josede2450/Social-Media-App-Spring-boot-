package com.socialmedia.backend.controllers;

import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{userId}")
    public List<NotificationDTO> getNotifications(@PathVariable Long userId) {
        return notificationRepository
                .findByUserUserIdOrderByCreatedDateDesc(userId)
                .stream()
                .map(notification -> new NotificationDTO(
                        notification.getNotificationId(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.getCreatedDate()
                ))
                .toList();
    }

    @Getter
    @AllArgsConstructor
    public static class NotificationDTO {
        private Long notificationId;
        private String message;
        private String type;
        private LocalDateTime createdDate;
    }
}