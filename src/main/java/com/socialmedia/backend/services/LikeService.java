package com.socialmedia.backend.services;

import com.socialmedia.backend.entities.Like;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.LikeRepository;
import com.socialmedia.backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService; // ✅ Use UserService instead of UserRepository

    public void likePost(Long postId) {

        // ✅ Get authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        if (likeRepository.existsByPostPostIdAndUserUserId(
                postId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post already liked");
        }

        Like like = Like.builder()
                .post(post)
                .user(currentUser)
                .createdDate(LocalDateTime.now())
                .build();

        likeRepository.save(like);
    }

    public void unlikePost(Long postId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!likeRepository.existsByPostPostIdAndUserUserId(
                postId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Like not found");
        }

        likeRepository.deleteByPostPostIdAndUserUserId(
                postId,
                currentUser.getUserId()
        );
    }
}