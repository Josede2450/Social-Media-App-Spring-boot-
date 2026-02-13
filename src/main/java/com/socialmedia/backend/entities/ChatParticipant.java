package com.socialmedia.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatParticipantId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
