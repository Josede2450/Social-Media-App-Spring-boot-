package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.MessageResponseDTO;
import com.socialmedia.backend.dtos.SendMessageDTO;
import com.socialmedia.backend.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // ✅ Create chat between authenticated user and target user
    @PostMapping("/create/{targetUserId}")
    public Long createChat(@PathVariable Long targetUserId) {
        return chatService.createChat(targetUserId);
    }

    // ✅ Sender is always authenticated user
    @PostMapping("/{chatId}/messages")
    public void sendMessage(
            @PathVariable Long chatId,
            @RequestBody SendMessageDTO dto) {

        chatService.sendMessage(chatId, dto.getContent());
    }

    // ✅ Only participants can view messages
    @GetMapping("/{chatId}/messages")
    public List<MessageResponseDTO> getMessages(@PathVariable Long chatId) {
        return chatService.getMessages(chatId);
    }

    // ✅ Get chats of authenticated user only
    @GetMapping("/me")
    public List<Long> getMyChats() {
        return chatService.getUserChats();
    }
}