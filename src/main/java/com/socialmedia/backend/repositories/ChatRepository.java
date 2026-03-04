package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}