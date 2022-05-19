package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.PasswordMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Passwords aren't the same.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
