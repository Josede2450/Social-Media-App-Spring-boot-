package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.CreatePostDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }



    public PostResponseDTO createPost(CreatePostDTO dto) {
        //TODO: once oauth is setup, allow for posts authors to be fetched.

        Post post = Post.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .contentUrl(dto.getContentUrl())
                .createdDate(LocalDateTime.now())
                .build();

        return convertToDTO(postRepository.save(post));
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return convertToDTO(post);
    }

    //TODO: ENSURE THAT ONLY OWNER CAN DELETE POST
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(id);
    }

    private PostResponseDTO convertToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPostId(post.getPostId());
        dto.setUsername(post.getUser() != null ? post.getUser().getUsername() : "unknown"); // null check
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContentUrl(post.getContentUrl());
        dto.setCreatedDate(post.getCreatedDate());
        dto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
        dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);
        return dto;
    }
}
