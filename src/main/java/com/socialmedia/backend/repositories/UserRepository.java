package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByGoogleId(String googleId);


}