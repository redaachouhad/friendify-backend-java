package com.example.friendify_backend_java.service.impl;

import com.example.friendify_backend_java.entity.Post;
import com.example.friendify_backend_java.repository.PostRepository;
import com.example.friendify_backend_java.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    final private PostRepository postRepository;
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
