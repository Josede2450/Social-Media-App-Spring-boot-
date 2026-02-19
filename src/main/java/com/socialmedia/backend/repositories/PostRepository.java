package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{

//    @Query(value = "SELECT * FROM posts", nativeQuery = true)
//    List<Post> findAllPosts();


}
