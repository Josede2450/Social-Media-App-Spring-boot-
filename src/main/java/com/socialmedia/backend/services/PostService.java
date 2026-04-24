package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.LikeRepository;
import com.socialmedia.backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeRepository likeRepository;

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PostResponseDTO createPost(String title, String description, MultipartFile image) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        String contentUrl = null;
        if (image != null && !image.isEmpty()) {
            contentUrl = savePostImage(image);
        }

        Post post = Post.builder()
                .title(title)
                .description(description)
                .contentUrl(contentUrl)
                .createdDate(LocalDateTime.now())
                .user(currentUser)
                .build();

        Post saved = postRepository.save(post);

        return convertToDTO(saved);
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return convertToDTO(post);
    }

    public PostResponseDTO updatePost(Long id, String title, String description, MultipartFile image) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own posts");
        }

        post.setTitle(title);
        post.setDescription(description);

        if (image != null && !image.isEmpty()) {
            post.setContentUrl(savePostImage(image));
        }

        Post updated = postRepository.save(post);

        return convertToDTO(updated);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    private String savePostImage(MultipartFile file) {
        try {
            String uploadDir = "uploads/post-images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:8080/uploads/post-images/" + filename;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save post image"
            );
        }
    }

    private PostResponseDTO convertToDTO(Post post) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        boolean likedByCurrentUser = likeRepository.existsByPostPostIdAndUserUserId(
                post.getPostId(),
                currentUser.getUserId()
        );

        User postUser = post.getUser();

        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .userId(postUser != null ? postUser.getUserId() : null)
                .username(postUser != null ? postUser.getUsername() : "unknown")
                .firstName(postUser != null ? postUser.getFirstName() : null)
                .lastName(postUser != null ? postUser.getLastName() : null)
                .profilePictureUrl(postUser != null ? postUser.getProfilePictureUrl() : null)
                .title(post.getTitle())
                .description(post.getDescription())
                .contentUrl(post.getContentUrl())
                .createdDate(post.getCreatedDate())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .likeCount(post.getLikes() != null ? post.getLikes().size() : 0)
                .likedByCurrentUser(likedByCurrentUser)
                .build();
    }
}