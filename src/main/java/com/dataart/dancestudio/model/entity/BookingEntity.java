package com.dataart.dancestudio.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingEntity {

    private final Integer id;

    private final Integer userId;

    private final Integer lessonId;

    private final Boolean isDeleted;

    public static BookingEntityBuilder builder() {
        return new BookingEntityBuilder();
    }

    public static class BookingEntityBuilder {

        private Integer id;

        private Integer userId;

        private Integer lessonId;

        private Boolean isDeleted;

        private boolean isUsed = false;

        public BookingEntityBuilder() {
        }

        public BookingEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public BookingEntityBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public BookingEntityBuilder lessonId(Integer lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public BookingEntityBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public BookingEntity build(){
            if (!isUsed) {
                isUsed = true;
                return new BookingEntity(id, userId, lessonId, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
