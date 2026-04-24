package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.FollowUserResponseDTO;
import com.socialmedia.backend.entities.Follow;
import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.FollowRepository;
import com.socialmedia.backend.repositories.NotificationRepository;
import com.socialmedia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

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

    public boolean isFollowing(Long followingId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        return followRepository.existsByFollowerUserIdAndFollowingUserId(
                currentUser.getUserId(),
                followingId
        );
    }

    // ✅ FIXED: Returns DTO instead of User
    public List<FollowUserResponseDTO> getFollowers(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return followRepository.findByFollowingUserId(userId)
                .stream()
                .map(f -> FollowUserResponseDTO.builder()
                        .userId(f.getFollower().getUserId())
                        .username(f.getFollower().getUsername())
                        .firstName(f.getFollower().getFirstName())
                        .lastName(f.getFollower().getLastName())
                        .profilePictureUrl(f.getFollower().getProfilePictureUrl())
                        .build())
                .toList();
    }

    // ✅ FIXED: Returns DTO instead of User
    public List<FollowUserResponseDTO> getFollowing(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return followRepository.findByFollowerUserId(userId)
                .stream()
                .map(f -> FollowUserResponseDTO.builder()
                        .userId(f.getFollowing().getUserId())
                        .username(f.getFollowing().getUsername())
                        .firstName(f.getFollowing().getFirstName())
                        .lastName(f.getFollowing().getLastName())
                        .profilePictureUrl(f.getFollowing().getProfilePictureUrl())
                        .build())
                .toList();
    }
}