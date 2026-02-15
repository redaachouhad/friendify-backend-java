package com.example.friendify_backend_java.repository;

import com.example.friendify_backend_java.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
