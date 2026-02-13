package com.socialmedia.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;

    private String phoneNumber;

    @Column(length = 500)
    private String bio;

    private String profilePictureUrl;

    private String role;

    private LocalDateTime dateCreated;

    private LocalDateTime lastLogin;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
}
