package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.ImageValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ImageValid, String> {

    @Override
    public boolean isValid(final String base64StringImage, final ConstraintValidatorContext context) {
        final byte[] bytes = Base64.getDecoder().decode(base64StringImage);
        final int imageSize = bytes.length;
        final double limitSize = 2 * Math.pow(10, 6);

        final List<String> messages = new ArrayList<>();
        boolean result = true;
        if (base64StringImage == null) {
            messages.add("Image File is null.");
            result = false;
        }

        if (imageSize > limitSize) {
            messages.add("Image File is too big.");
            result = false;
        }

        final String messageTemplate = String.join(", ", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return result;
    }

}
