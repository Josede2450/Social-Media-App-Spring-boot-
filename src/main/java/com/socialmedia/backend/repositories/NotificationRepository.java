package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUserIdOrderByCreatedDateDesc(Long userId);
}