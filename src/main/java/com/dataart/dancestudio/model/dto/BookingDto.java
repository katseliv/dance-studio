package com.dataart.dancestudio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
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

    public static BookingDtoBuilder builder() {
        return new BookingDtoBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookingDtoBuilder {

        private Integer userId;
        private Integer lessonId;

        private boolean isUsed = false;

        public BookingDtoBuilder() {
        }

        public BookingDtoBuilder userId(final Integer userId) {
            this.userId = userId;
            return this;
        }

        public BookingDtoBuilder lessonId(final Integer lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public BookingDto build() {
            if (!isUsed) {
                isUsed = true;
                return new BookingDto(userId, lessonId);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
