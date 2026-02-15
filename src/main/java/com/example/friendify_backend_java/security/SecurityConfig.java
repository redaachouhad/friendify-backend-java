package com.example.friendify_backend_java.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity // Enables Spring Security’s web security support
@EnableMethodSecurity // Enables method-level security annotations (@PreAuthorize, etc.)
@RequiredArgsConstructor // Lombok: generates constructor for final fields
public class SecurityConfig {

    // Custom JWT filter that validates token on each request
    private final JwtRequestFilter jwtRequestFilter;

    /**
     * PasswordEncoder bean.
     * BCrypt is used to hash user passwords before storing them in the database.
     * Spring Security will use this automatically during authentication.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes AuthenticationManager as a Spring Bean.
     * It is used in the login process to authenticate username/password.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)  {
        return configuration.getAuthenticationManager();
    }

    /**
     * Main Security Filter Chain configuration.
     * This replaces the old WebSecurityConfigurerAdapter (removed in Spring Security 6).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http
                .cors(Customizer.withDefaults())
                // Disable CSRF because we are using JWT (stateless API)
                // CSRF protection is mainly needed for session-based authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Allow H2 console frames
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))


                // Configure endpoint authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to login & register endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/h2-console/**"
                        ).permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Configure session management
                // STATELESS means Spring Security will NOT create or use HTTP sessions
                // Each request must carry its own JWT token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add our custom JWT filter before the default authentication filter
        // This ensures JWT validation happens before Spring processes authentication
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the security filter chain
        return http.build();
    }
}
