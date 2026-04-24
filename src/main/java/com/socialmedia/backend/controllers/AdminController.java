package com.socialmedia.backend.controllers;

import com.socialmedia.backend.entities.*;
import com.socialmedia.backend.repositories.*;
import com.socialmedia.backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Transactional
public class AdminController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final UserService userService; // ✅ NEW

    // ✅ ADMIN CHECK
    private void requireAdmin() {
        User currentUser = userService.getCurrentAuthenticatedUser();

        System.out.println("ADMIN CHECK USER ID: " + currentUser.getUserId());
        System.out.println("ADMIN CHECK EMAIL: " + currentUser.getEmail());
        System.out.println("ADMIN CHECK USERNAME: " + currentUser.getUsername());
        System.out.println("ADMIN CHECK ROLE: " + currentUser.getRole());

        if (currentUser.getRole() == null || !currentUser.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }

    // ================= POSTS =================

    @GetMapping("/posts")
    public List<AdminPostDTO> getAllPosts() {
        requireAdmin();

        return postRepository.findAll()
                .stream()
                .map(this::toAdminPostDTO)
                .toList();
    }

    @PutMapping("/posts/{postId}")
    public AdminPostDTO updatePost(
            @PathVariable Long postId,
            @RequestBody AdminPostUpdateRequest request) {

        requireAdmin();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getDescription() != null) post.setDescription(request.getDescription());

        return toAdminPostDTO(postRepository.save(post));
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        requireAdmin();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        postRepository.delete(post);
    }

    // ================= COMMENTS =================

    @PutMapping("/comments/{commentId}")
    public AdminCommentDTO updateComment(
            @PathVariable Long commentId,
            @RequestBody AdminCommentUpdateRequest request) {

        requireAdmin();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found")
                );

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }

        return toAdminCommentDTO(commentRepository.save(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        requireAdmin();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found")
                );

        commentRepository.delete(comment);
    }

    // ================= REPORTS =================

    @GetMapping("/reports")
    public List<AdminReportDTO> getAllReports() {
        requireAdmin();

        return reportRepository.findAll()
                .stream()
                .map(this::toAdminReportDTO)
                .toList();
    }

    @DeleteMapping("/reports/{reportId}")
    public void deleteReport(@PathVariable Long reportId) {
        requireAdmin();

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found")
                );

        reportRepository.delete(report);
    }

    @DeleteMapping("/reports/{reportId}/post")
    public void deleteReportedPost(@PathVariable Long reportId) {
        requireAdmin();

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found")
                );

        Post post = report.getPost();

        reportRepository.delete(report);

        if (post != null) {
            postRepository.delete(post);
        }
    }

    // ================= USERS =================

    @GetMapping("/users")
    public List<AdminUserDTO> getAllUsers() {
        requireAdmin();

        return userRepository.findAll()
                .stream()
                .map(this::toAdminUserDTO)
                .toList();
    }

    @PutMapping("/users/{userId}")
    public AdminUserDTO updateUser(
            @PathVariable Long userId,
            @RequestBody AdminUserUpdateRequest request) {

        requireAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());
        if (request.getRole() != null) user.setRole(request.getRole());

        return toAdminUserDTO(userRepository.save(user));
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        requireAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        userRepository.delete(user);
    }

    // ================= MAPPERS =================

    private AdminPostDTO toAdminPostDTO(Post post) {
        User user = post.getUser();

        List<AdminCommentDTO> comments = commentRepository
                .findByPostPostId(post.getPostId())
                .stream()
                .map(this::toAdminCommentDTO)
                .toList();

        return new AdminPostDTO(
                post.getPostId(),
                user != null ? user.getUserId() : null,
                user != null ? user.getUsername() : "unknown",
                user != null ? user.getFirstName() : null,
                user != null ? user.getLastName() : null,
                post.getTitle(),
                post.getDescription(),
                post.getContentUrl(),
                post.getCreatedDate(),
                post.getLikes() != null ? post.getLikes().size() : 0,
                comments.size(),
                comments
        );
    }

    private AdminCommentDTO toAdminCommentDTO(Comment comment) {
        User user = comment.getUser();

        return new AdminCommentDTO(
                comment.getCommentId(),
                comment.getPost().getPostId(),
                user != null ? user.getUserId() : null,
                user != null ? user.getUsername() : "unknown",
                user != null ? user.getFirstName() : null,
                user != null ? user.getLastName() : null,
                comment.getContent(),
                comment.getCreatedDate()
        );
    }

    private AdminReportDTO toAdminReportDTO(Report report) {
        Post post = report.getPost();
        User reporter = report.getReporter();

        return new AdminReportDTO(
                report.getReportId(),
                post != null ? post.getPostId() : null,
                post != null ? post.getDescription() : null,
                reporter != null ? reporter.getUserId() : null,
                reporter != null ? reporter.getUsername() : "unknown",
                report.getReason()
        );
    }

    private AdminUserDTO toAdminUserDTO(User user) {
        return new AdminUserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBio(),
                user.getPhoneNumber(),
                user.getProfilePictureUrl(),
                user.getRole(),
                user.getDateCreated(),
                user.getLastLogin()
        );
    }

    // ================= DTOs =================

    @Getter @Setter @NoArgsConstructor
    public static class AdminPostUpdateRequest {
        private String title;
        private String description;
    }

    @Getter @Setter @NoArgsConstructor
    public static class AdminCommentUpdateRequest {
        private String content;
    }

    @Getter @Setter @NoArgsConstructor
    public static class AdminUserUpdateRequest {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String bio;
        private String phoneNumber;
        private String profilePictureUrl;
        private String role;
    }

    @Getter @AllArgsConstructor
    public static class AdminPostDTO {
        private Long postId;
        private Long userId;
        private String username;
        private String firstName;
        private String lastName;
        private String title;
        private String description;
        private String contentUrl;
        private LocalDateTime createdDate;
        private int likeCount;
        private int commentCount;
        private List<AdminCommentDTO> comments;
    }

    @Getter @AllArgsConstructor
    public static class AdminCommentDTO {
        private Long commentId;
        private Long postId;
        private Long userId;
        private String username;
        private String firstName;
        private String lastName;
        private String content;
        private LocalDateTime createdDate;
    }

    @Getter @AllArgsConstructor
    public static class AdminReportDTO {
        private Long reportId;
        private Long postId;
        private String postContent;
        private Long reporterId;
        private String reporterUsername;
        private String reason;
    }

    @Getter @AllArgsConstructor
    public static class AdminUserDTO {
        private Long userId;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String bio;
        private String phoneNumber;
        private String profilePictureUrl;
        private String role;
        private LocalDateTime dateCreated;
        private LocalDateTime lastLogin;
    }
}