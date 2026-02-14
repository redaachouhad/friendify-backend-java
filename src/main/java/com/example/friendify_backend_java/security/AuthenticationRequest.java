package com.example.friendify_backend_java.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// authentication request contains password and username
public class AuthenticationRequest {
    private String email;
    private String password;
}
