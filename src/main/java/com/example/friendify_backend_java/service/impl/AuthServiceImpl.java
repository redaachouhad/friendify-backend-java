package com.example.friendify_backend_java.service.impl;

import com.example.friendify_backend_java.dto.LoginUserRequest;
import com.example.friendify_backend_java.dto.RegisterUserRequest;
import com.example.friendify_backend_java.entity.User;
import com.example.friendify_backend_java.enums.ErrorCode;
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
                    ErrorCode.CONFLICT_ERROR,
                    HttpStatus.CONFLICT,
                    "Email already exists"
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
    public String login(LoginUserRequest userRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getEmail(),
                        userRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal(); // cast is safe if using UserDetailsService

        return jwtUtil.generateToken(user);
    }

}
