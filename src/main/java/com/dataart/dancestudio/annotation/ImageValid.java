package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.ImageFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ImageValid {

    String message() default "Image file invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
