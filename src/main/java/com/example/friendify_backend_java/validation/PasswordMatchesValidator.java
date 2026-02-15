package com.example.friendify_backend_java.validation;

import com.example.friendify_backend_java.dto.RegisterUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserRequest> {
    @Override
    public boolean isValid(RegisterUserRequest request,
                           ConstraintValidatorContext context) {

        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return true; // let @NotBlank handle null
        }

        // handle the error matching password
        boolean matches = request.getPassword().equals(request.getConfirmPassword());

        if (!matches) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            context.getDefaultConstraintMessageTemplate()
                    )
                    .addPropertyNode("confirmPassword") // ✅ attach to field
                    .addConstraintViolation();
        }

        return matches;
    }
}
