package com.example.friendify_backend_java.security;

import com.example.friendify_backend_java.entity.User;
import com.example.friendify_backend_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// this service is implemented to load the user information from the database
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found with email: " + email));
    }

}
