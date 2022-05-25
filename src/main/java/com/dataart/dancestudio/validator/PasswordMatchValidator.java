package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.PasswordMatch;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegistrationDto> {

    @Override
    public boolean isValid(final UserRegistrationDto userRegistrationDto, final ConstraintValidatorContext context) {
        final String password = userRegistrationDto.getPassword();
        final String passwordConfirmation = userRegistrationDto.getPasswordConfirmation();
        return password != null && password.equals(passwordConfirmation);
    }

}
