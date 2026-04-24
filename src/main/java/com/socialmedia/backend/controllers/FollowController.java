package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.FollowUserResponseDTO;
import com.socialmedia.backend.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingId}")
    public void follow(@PathVariable Long followingId) {
        followService.followUser(followingId);
    }

    @DeleteMapping("/{followingId}")
    public void unfollow(@PathVariable Long followingId) {
        followService.unfollowUser(followingId);
    }

    @GetMapping("/status/{followingId}")
    public boolean isFollowing(@PathVariable Long followingId) {
        return followService.isFollowing(followingId);
    }

    // ✅ FIXED
    @GetMapping("/followers/{userId}")
    public List<FollowUserResponseDTO> getFollowers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    // ✅ FIXED
    @GetMapping("/following/{userId}")
    public List<FollowUserResponseDTO> getFollowing(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }
}