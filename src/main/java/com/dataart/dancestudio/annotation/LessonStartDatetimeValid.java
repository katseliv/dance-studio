package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.LessonStartDatetimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LessonStartDatetimeValidator.class)
public @interface LessonStartDatetimeValid {

    String message() default "Datetime invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
