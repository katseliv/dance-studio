package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookingDto {

    private final Integer id;
    private final Integer userId;
    private final Integer lessonId;
    private final Boolean isDeleted;

    public static BookingDtoBuilder builder() {
        return new BookingDtoBuilder();
    }

    public static class BookingDtoBuilder {

        private Integer id;
        private Integer userId;
        private Integer lessonId;
        private Boolean isDeleted;

        private boolean isUsed = false;

        public BookingDtoBuilder() {
        }

        public BookingDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public BookingDtoBuilder userId(final Integer userId) {
            this.userId = userId;
            return this;
        }

        public BookingDtoBuilder lessonId(final Integer lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public BookingDtoBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public BookingDto build(){
            if (!isUsed) {
                isUsed = true;
                return new BookingDto(id, userId, lessonId, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
