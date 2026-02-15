package com.example.friendify_backend_java.controller;

import com.example.friendify_backend_java.dto.LoginUserRequest;
import com.example.friendify_backend_java.dto.RegisterUserRequest;
import com.example.friendify_backend_java.service.impl.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    protected final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    /**
     * Register Api
     * @param user
     * @return
     */

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

    /**
     * Login Api
     * @param userRequest
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody @Valid LoginUserRequest userRequest,
            HttpServletResponse response
    ) {
        // 1. Authenticate and generate JWT
        String jwt = authServiceImpl.login(userRequest, response);

        // 2. Return success response
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "jwt", jwt
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        return ResponseEntity.ok(authentication.getPrincipal());
    }

}
