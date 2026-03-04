package com.socialmedia.backend.services;

import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.Report;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.PostRepository;
import com.socialmedia.backend.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserService userService; // ✅ Use UserService instead

    public void reportPost(Long postId, String reason) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        if (reportRepository.existsByPostPostIdAndReporterUserId(
                postId,
                currentUser.getUserId()
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post already reported");
        }

        Report report = Report.builder()
                .post(post)
                .reporter(currentUser)
                .reason(reason)
                .build();

        reportRepository.save(report);
    }
}