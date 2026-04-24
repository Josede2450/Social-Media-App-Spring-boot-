package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    List<ChatParticipant> findByUserUserId(Long userId);

    List<ChatParticipant> findByChatChatId(Long chatId);

    boolean existsByChatChatIdAndUserUserId(Long chatId, Long userId);
}