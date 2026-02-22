package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserUserId(Long userId);
}