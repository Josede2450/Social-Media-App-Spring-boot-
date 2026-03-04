package com.socialmedia.backend.controllers;

import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedDateDesc(userId);
    }
}