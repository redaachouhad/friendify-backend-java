package com.example.friendify_backend_java.controller;

import com.example.friendify_backend_java.entity.Post;
import com.example.friendify_backend_java.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    final private PostServiceImpl postService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(){

        List<Post> posts = postService.getAllPosts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(posts);
    }
}
