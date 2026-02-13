package com.socialmedia.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatParticipant> participants;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;
}
