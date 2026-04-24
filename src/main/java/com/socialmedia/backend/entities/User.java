package com.socialmedia.backend.entities;

// 🔹 JPA imports
import jakarta.persistence.*;

// 🔹 Lombok imports
import lombok.*;

// 🔹 Date-time import
import java.time.LocalDateTime;

// 🔹 List import
import java.util.List;

// 🔹 Mark this as a JPA entity
@Entity

// 🔹 Map to users table
@Table(name = "users")

// 🔹 Lombok getter generation
@Getter

// 🔹 Lombok setter generation
@Setter

// 🔹 Lombok no-args constructor
@NoArgsConstructor

// 🔹 Lombok all-args constructor
@AllArgsConstructor

// 🔹 Lombok builder pattern
@Builder
public class User {

    // 🔹 Primary key
    @Id

    // 🔹 Auto-increment ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 🔹 Google ID, nullable for now
    @Column(unique = true)
    private String googleId;

    // 🔹 Email is required and unique
    @Column(nullable = false, unique = true)
    private String email;

    // 🔹 First name
    private String firstName;

    // 🔹 Last name
    private String lastName;

    // 🔹 Username must be unique
    @Column(unique = true)
    private String username;

    // 🔹 Optional phone number
    private String phoneNumber;

    // 🔹 User bio with max length 500
    @Column(length = 500)
    private String bio;

    // 🔹 Profile picture URL
    private String profilePictureUrl;

    // 🔹 User role
    private String role;

    // 🔹 Date account was created
    private LocalDateTime dateCreated;

    // 🔹 Last login time
    private LocalDateTime lastLogin;

    // 🔹 One user can have many posts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    // 🔹 Automatically set creation date before insert
    @PrePersist
    public void prePersist() {

        // 🔹 Set current time
        this.dateCreated = LocalDateTime.now();
    }
}