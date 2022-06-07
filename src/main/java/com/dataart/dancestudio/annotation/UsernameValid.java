package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameValid {

    String message() default "Such username already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
