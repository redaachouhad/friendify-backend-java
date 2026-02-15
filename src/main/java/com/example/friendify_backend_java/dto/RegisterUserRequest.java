package com.example.friendify_backend_java.dto;

import com.example.friendify_backend_java.enums.Gender;
import com.example.friendify_backend_java.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@PasswordMatches
public class RegisterUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String pseudonym;

    @NotBlank(message = "First name is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @JsonProperty("last_name")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @JsonProperty("password_confirmation")
    private String confirmPassword;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number is invalid")
    private String phone; // optional

    @NotNull(message = "Birth date is required")
    @JsonProperty("birth_date")
    private LocalDate birthDate;


}
