package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.UsernameValid;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        final List<String> messages = new ArrayList<>();
        if (userRepository.existsByUsername(username)) {
            messages.add("Username exists.");
        }
        return messages.size() == 0;
    }

}
