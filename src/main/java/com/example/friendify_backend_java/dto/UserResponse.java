package com.example.friendify_backend_java.dto;

import com.example.friendify_backend_java.enums.Gender;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String pseudonym,
        String email,
        Gender gender
) {}
