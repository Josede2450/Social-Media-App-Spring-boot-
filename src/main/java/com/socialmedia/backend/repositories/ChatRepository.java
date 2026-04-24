package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
        SELECT c
        FROM Chat c
        JOIN ChatParticipant p1 ON p1.chat.chatId = c.chatId
        JOIN ChatParticipant p2 ON p2.chat.chatId = c.chatId
        WHERE p1.user.userId = :userId1
          AND p2.user.userId = :userId2
    """)
    Optional<Chat> findPrivateChatBetweenUsers(Long userId1, Long userId2);
}