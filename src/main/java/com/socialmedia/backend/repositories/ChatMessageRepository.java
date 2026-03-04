package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatChatIdOrderByCreatedDateAsc(Long chatId);
}