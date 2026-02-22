package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.PostRequestDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.PostRepository;
import com.socialmedia.backend.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CREATE POST
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO request,
                                      Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setContentUrl(request.getContentUrl());
        post.setCreatedDate(LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);

        return buildPostResponse(post);
    }

    // GET ALL POSTS
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::buildPostResponse)
                .collect(Collectors.toList());
    }

    // GET POST BY ID
    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return buildPostResponse(post);
    }

    // DELETE POST (OWNER ONLY)
    @Transactional
    public void deletePost(Long postId, Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    // HELPER: BUILD DTO
    private PostResponseDTO buildPostResponse(Post post) {

        PostResponseDTO dto = new PostResponseDTO();

        dto.setPostId(post.getPostId());
        dto.setUsername(post.getUser().getUsername()); // or getEmail()
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContentUrl(post.getContentUrl());
        dto.setCreatedDate(post.getCreatedDate());

        dto.setCommentCount(
                post.getComments() == null ? 0 : post.getComments().size()
        );

        dto.setLikeCount(
                post.getLikes() == null ? 0 : post.getLikes().size()
        );

        return dto;
    }

    // HELPER: GET AUTHENTICATED USER
    private User getAuthenticatedUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String googleId = oauthUser.getAttribute("sub");

        return userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}