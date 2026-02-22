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


    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(unique = true, nullable = true)
    private String googleId;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Column(length = 500)
    private String bio;

    private String profilePictureUrl;

    @Builder.Default
    private String role = "USER";

    private LocalDateTime dateCreated;
    private LocalDateTime lastLogin;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
    }
}
