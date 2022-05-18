package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface PasswordValid {

    String message() default "Password invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
