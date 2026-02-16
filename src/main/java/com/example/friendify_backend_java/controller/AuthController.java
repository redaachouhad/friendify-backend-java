package com.example.friendify_backend_java.controller;

import com.example.friendify_backend_java.dto.LoginUserRequest;
import com.example.friendify_backend_java.dto.RegisterUserRequest;
import com.example.friendify_backend_java.dto.UserResponse;
import com.example.friendify_backend_java.entity.User;
import com.example.friendify_backend_java.enums.ErrorCode;
import com.example.friendify_backend_java.exception.ErrorResponse;
import com.example.friendify_backend_java.service.impl.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    protected final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid RegisterUserRequest user
    ) {
        authServiceImpl.register(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "success", true,
                                "message", "User registered successfully"
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody @Valid LoginUserRequest userRequest,
            HttpServletResponse response
    ) {
        // 1. Authenticate and generate JWT
        String jwt = authServiceImpl.login(userRequest);

        // 2. Create HTTP-only cookie
        Cookie cookie = new Cookie("token", jwt);
        cookie.setHttpOnly(true);           // Prevents JS access
        // cookie.setSecure(true);          // Enable in production (HTTPS only)
        cookie.setPath("/");                 // Available for entire domain
        cookie.setMaxAge(86400);          // 1 hour
        response.addCookie(cookie);

        // 3. Return success response
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        Map.of(
                                "success", true,
                                "message", "Login successful",
                                "jwt", jwt
                        )
                );

    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Cast the principal to your User entity
        User user = (User) authentication.getPrincipal();

        // Map only the data you want to send to the frontend
        UserResponse response = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPseudonym(),
                user.getEmail(),
                user.getGender()
        );

        return ResponseEntity.ok(response);
    }


}
