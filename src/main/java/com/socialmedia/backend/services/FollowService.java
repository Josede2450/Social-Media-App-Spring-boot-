package com.socialmedia.backend.services;

import com.socialmedia.backend.entities.*;
import com.socialmedia.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService; // ✅ Add this

    public void followUser(Long followingId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser.getUserId().equals(followingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot follow yourself");
        }

        if (followRepository.existsByFollowerUserIdAndFollowingUserId(
                currentUser.getUserId(),
                followingId
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already following");
        }

        User targetUser = userRepository.findById(followingId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        Follow follow = Follow.builder()
                .follower(currentUser)
                .following(targetUser)
                .build();

        followRepository.save(follow);

        // 🔔 Create notification for target user
        Notification notification = Notification.builder()
                .user(targetUser)
                .type("FOLLOW")
                .message(currentUser.getUsername() + " started following you")
                .createdDate(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    public void unfollowUser(Long followingId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!followRepository.existsByFollowerUserIdAndFollowingUserId(
                currentUser.getUserId(),
                followingId
        )) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not following");
        }

        followRepository.deleteByFollowerUserIdAndFollowingUserId(
                currentUser.getUserId(),
                followingId
        );
    }
}