package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.ChatResponseDTO;
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

    @PostMapping("/create/{targetUserId}")
    public Long createChat(@PathVariable Long targetUserId) {
        return chatService.createOrReuseChat(targetUserId);
    }

    @PostMapping("/{chatId}/messages")
    public void sendMessage(
            @PathVariable Long chatId,
            @RequestBody SendMessageDTO dto) {

        chatService.sendMessage(chatId, dto.getContent());
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageResponseDTO> getMessages(@PathVariable Long chatId) {
        return chatService.getMessages(chatId);
    }

    @GetMapping("/me")
    public List<ChatResponseDTO> getMyChats() {
        return chatService.getUserChats();
    }
}