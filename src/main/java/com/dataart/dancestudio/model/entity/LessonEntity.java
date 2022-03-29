package com.dataart.dancestudio.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LessonEntity {

    private final Integer id;
    private final Integer userTrainerId;
    private final Integer danceStyleId;
    private final LocalDateTime startDatetime;
    private final Integer duration;
    private final Integer roomId;
    private final Boolean isDeleted;

    public static LessonEntityBuilder builder() {
        return new LessonEntityBuilder();
    }

    public static class LessonEntityBuilder {

        private Integer id;
        private Integer userTrainerId;
        private Integer danceStyleId;
        private LocalDateTime startDatetime;
        private Integer duration;
        private Integer roomId;
        private Boolean isDeleted;

        private boolean isUsed = false;

        public LessonEntityBuilder() {
        }

        public LessonEntityBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public LessonEntityBuilder userTrainerId(final Integer userTrainerId) {
            this.userTrainerId = userTrainerId;
            return this;
        }

        public LessonEntityBuilder danceStyleId(final Integer danceStyleId) {
            this.danceStyleId = danceStyleId;
            return this;
        }

        public LessonEntityBuilder startDatetime(final LocalDateTime startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonEntityBuilder duration(final Integer duration) {
            this.duration = duration;
            return this;
        }

        public LessonEntityBuilder roomId(final Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public LessonEntityBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public LessonEntity build() {
            if (!isUsed) {
                isUsed = true;
                return new LessonEntity(id, userTrainerId, danceStyleId, startDatetime, duration, roomId, isDeleted);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}

