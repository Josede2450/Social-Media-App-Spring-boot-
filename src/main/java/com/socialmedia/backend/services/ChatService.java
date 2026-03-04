package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.MessageResponseDTO;
import com.socialmedia.backend.entities.*;
import com.socialmedia.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository participantRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService; // ✅ Add this

    // ✅ Create private chat between logged-in user and target user
    public Long createChat(Long targetUserId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser.getUserId().equals(targetUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create chat with yourself");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found")
                );

        Chat chat = chatRepository.save(Chat.builder().build());

        participantRepository.save(ChatParticipant.builder()
                .chat(chat)
                .user(currentUser)
                .build());

        participantRepository.save(ChatParticipant.builder()
                .chat(chat)
                .user(targetUser)
                .build());

        return chat.getChatId();
    }

    // ✅ Sender is always authenticated user
    public void sendMessage(Long chatId, String content) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!participantRepository.existsByChatChatIdAndUserUserId(
                chatId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not in chat");
        }

        ChatMessage message = ChatMessage.builder()
                .chat(chatRepository.getReferenceById(chatId))
                .sender(currentUser)
                .content(content)
                .createdDate(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }

    // ✅ Only allow access if user belongs to chat
    public List<MessageResponseDTO> getMessages(Long chatId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!participantRepository.existsByChatChatIdAndUserUserId(
                chatId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not in chat");
        }

        return messageRepository.findByChatChatIdOrderByCreatedDateAsc(chatId)
                .stream()
                .map(msg -> MessageResponseDTO.builder()
                        .messageId(msg.getMessageId())
                        .senderId(msg.getSender().getUserId())
                        .senderUsername(msg.getSender().getUsername())
                        .content(msg.getContent())
                        .createdDate(msg.getCreatedDate())
                        .build())
                .toList();
    }

    // ✅ Get chats of authenticated user only
    public List<Long> getUserChats() {

        User currentUser = userService.getCurrentAuthenticatedUser();

        return participantRepository.findByUserUserId(currentUser.getUserId())
                .stream()
                .map(p -> p.getChat().getChatId())
                .toList();
    }
}