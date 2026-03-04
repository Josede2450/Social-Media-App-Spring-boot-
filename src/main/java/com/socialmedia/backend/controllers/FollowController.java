package com.socialmedia.backend.controllers;

import com.socialmedia.backend.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // ✅ Follow target user (authenticated user is follower)
    @PostMapping("/{followingId}")
    public void follow(@PathVariable Long followingId) {
        followService.followUser(followingId);
    }

    // ✅ Unfollow target user
    @DeleteMapping("/{followingId}")
    public void unfollow(@PathVariable Long followingId) {
        followService.unfollowUser(followingId);
    }
}