package com.dataart.dancestudio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = BookingDto.BookingDtoBuilder.class)
public class BookingDto {

    @NotNull(message = "User Id is null.")
    @Positive(message = "User Id is negative ot zero.")
    private final Integer userId;

    @NotNull(message = "Lesson Id is null.")
    @Positive(message = "Lesson Id is negative ot zero.")
    private final Integer lessonId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookingDtoBuilder {

    }

}
