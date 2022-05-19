package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.DatetimeValid;
import com.dataart.dancestudio.model.dto.LessonDto;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class DatetimeValidator implements ConstraintValidator<DatetimeValid, LessonDto> {

    @Value("${datetime.limitDays}")
    private int limitDays;

    @Override
    public boolean isValid(final LessonDto lessonDto, final ConstraintValidatorContext context) {
        final LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());
        final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(lessonDto.getTimeZone()));
        final Instant datetimeInstant = localDateTimeZoned.toInstant();

        final LocalDateTime now = LocalDateTime.now().plusDays(limitDays);
        final ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
        final Instant nowInstant = nowZoned.toInstant();

        final List<String> messages = new ArrayList<>();
        if (datetimeInstant.isBefore(Instant.now())) {
            messages.add("Datetime is in the past.");
        }

        if (datetimeInstant.isBefore(nowInstant)) {
            messages.add("Datetime must be no later than " + limitDays + " day before the event.");
        }

        final String messageTemplate = String.join(", ", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return messages.size() == 0;
    }

}
