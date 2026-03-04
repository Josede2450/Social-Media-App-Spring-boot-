package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

    List<Follow> findByFollowerUserId(Long userId);

    List<Follow> findByFollowingUserId(Long userId);

    void deleteByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);
}