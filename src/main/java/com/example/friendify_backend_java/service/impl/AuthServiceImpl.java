package com.example.friendify_backend_java.service.impl;

import com.example.friendify_backend_java.dto.LoginUserRequest;
import com.example.friendify_backend_java.dto.RegisterUserRequest;
import com.example.friendify_backend_java.entity.User;
import com.example.friendify_backend_java.exception.BusinessException;
import com.example.friendify_backend_java.repository.UserRepository;
import com.example.friendify_backend_java.security.JwtUtil;
import com.example.friendify_backend_java.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * This function enable user to create the account.
     * @param user
     */
    @Transactional
    @Override
    public void register(RegisterUserRequest user){
        // verify the uniqueness of email
        if(userRepository.existsByEmail(user.getEmail())){
            throw new BusinessException(
                    "Email already exists",
                    HttpStatus.CONFLICT
            );
        }

        // creating the new user
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .pseudonym(user.getPseudonym())
                .password(passwordEncoder.encode(user.getPassword()))
                .gender(user.getGender())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .isActive(true)
                .build();

        // saving the user in database
        userRepository.save(newUser);
    }

    @Override
    public String login(
            LoginUserRequest userRequest,
            HttpServletResponse response
    ) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getEmail(),
                            userRequest.getPassword()
                    )
            );

            // getPrincipal is guaranteed to be non-null if authentication succeeds
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User user)) {
                throw new BusinessException("Invalid user details", HttpStatus.UNAUTHORIZED);
            }

            // Generate JWT token
            String jwt = jwtUtil.generateToken(user);

            // Create HTTP-only cookie
            Cookie cookie = new Cookie("token", jwt);
            cookie.setHttpOnly(true);           // Prevents JS access
            // cookie.setSecure(true);          // Enable in production (HTTPS only)
            cookie.setPath("/");                 // Available for entire domain
            cookie.setMaxAge(60 * 60);          // 1 hour
            response.addCookie(cookie);

            return jwt;

        } catch (Exception ex) {
            // Any authentication failure will bubble up here
            // Let the global exception handler deal with it
            throw new BusinessException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
