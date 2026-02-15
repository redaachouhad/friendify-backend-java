package com.example.friendify_backend_java.service;

import com.example.friendify_backend_java.dto.LoginUserRequest;
import com.example.friendify_backend_java.dto.RegisterUserRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public void register(RegisterUserRequest user);

    public String login(
            LoginUserRequest userRequest,
            HttpServletResponse response
            );
}
