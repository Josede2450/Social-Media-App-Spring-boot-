package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.CreatePostDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.PostRepository;
import com.socialmedia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PostResponseDTO createPost(CreatePostDTO dto) {

        // 🔥 Temporary default user (until OAuth is implemented)
        User defaultUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .contentUrl(dto.getContentUrl())
                .createdDate(LocalDateTime.now())
                .user(defaultUser) // ✅ REQUIRED
                .build();

        Post saved = postRepository.save(post);

        return convertToDTO(saved);
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return convertToDTO(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postRepository.delete(post);
    }

    private PostResponseDTO convertToDTO(Post post) {
        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .username(post.getUser() != null ? post.getUser().getUsername() : "unknown")
                .title(post.getTitle())
                .description(post.getDescription())
                .contentUrl(post.getContentUrl())
                .createdDate(post.getCreatedDate())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .likeCount(post.getLikes() != null ? post.getLikes().size() : 0)
                .build();
    }
}