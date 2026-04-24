package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.ChatResponseDTO;
import com.socialmedia.backend.dtos.MessageResponseDTO;
import com.socialmedia.backend.entities.*;
import com.socialmedia.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository participantRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // ✅ ADD THIS
    private final NotificationRepository notificationRepository;

    public Long createOrReuseChat(Long targetUserId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser.getUserId().equals(targetUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot create chat with yourself"
            );
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Target user not found"
                        )
                );

        Optional<Chat> existingChat = chatRepository.findPrivateChatBetweenUsers(
                currentUser.getUserId(),
                targetUser.getUserId()
        );

        if (existingChat.isPresent()) {
            return existingChat.get().getChatId();
        }

        Chat chat = Chat.builder().build();
        chat = chatRepository.save(chat);

        participantRepository.save(
                ChatParticipant.builder()
                        .chat(chat)
                        .user(currentUser)
                        .build()
        );

        participantRepository.save(
                ChatParticipant.builder()
                        .chat(chat)
                        .user(targetUser)
                        .build()
        );

        return chat.getChatId();
    }

    public void sendMessage(Long chatId, String content) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!participantRepository.existsByChatChatIdAndUserUserId(
                chatId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User not in chat"
            );
        }

        ChatMessage message = ChatMessage.builder()
                .chat(chatRepository.getReferenceById(chatId))
                .sender(currentUser)
                .content(content)
                .createdDate(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        // =========================
        // ✅ NEW: SEND NOTIFICATION
        // =========================
        List<ChatParticipant> participants =
                participantRepository.findByChatChatId(chatId);

        participants.stream()
                .map(ChatParticipant::getUser)
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .forEach(receiver -> {

                    Notification notification = Notification.builder()
                            .user(receiver)
                            .message(currentUser.getUsername() + " sent you a message")
                            .type("MESSAGE")
                            .createdDate(LocalDateTime.now())
                            .build();

                    notificationRepository.save(notification);
                });
    }

    public List<MessageResponseDTO> getMessages(Long chatId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!participantRepository.existsByChatChatIdAndUserUserId(
                chatId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User not in chat"
            );
        }

        return messageRepository.findByChatChatIdOrderByCreatedDateAsc(chatId)
                .stream()
                .map(msg -> MessageResponseDTO.builder()
                        .messageId(msg.getMessageId())
                        .senderId(msg.getSender().getUserId())
                        .senderUsername(msg.getSender().getUsername())
                        .senderFirstName(msg.getSender().getFirstName())
                        .senderLastName(msg.getSender().getLastName())
                        .content(msg.getContent())
                        .createdDate(msg.getCreatedDate())
                        .build())
                .toList();
    }

    public List<ChatResponseDTO> getUserChats() {

        User currentUser = userService.getCurrentAuthenticatedUser();

        return participantRepository.findByUserUserId(currentUser.getUserId())
                .stream()
                .map(participant -> {
                    Chat chat = participant.getChat();

                    List<ChatParticipant> participants =
                            participantRepository.findByChatChatId(chat.getChatId());

                    User otherUser = participants.stream()
                            .map(ChatParticipant::getUser)
                            .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Other chat participant not found"
                            ));

                    return ChatResponseDTO.builder()
                            .chatId(chat.getChatId())
                            .otherUserId(otherUser.getUserId())
                            .otherUsername(otherUser.getUsername())
                            .otherFirstName(otherUser.getFirstName())
                            .otherLastName(otherUser.getLastName())
                            .otherEmail(otherUser.getEmail())
                            .otherBio(otherUser.getBio())
                            .otherProfileImage(otherUser.getProfilePictureUrl())
                            .build();
                })
                .toList();
    }
}