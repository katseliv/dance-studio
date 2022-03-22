package com.dataart.dancestudio.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

        public LessonEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public LessonEntityBuilder userTrainerId(Integer userTrainerId) {
            this.userTrainerId = userTrainerId;
            return this;
        }

        public LessonEntityBuilder danceStyleId(Integer danceStyleId) {
            this.danceStyleId = danceStyleId;
            return this;
        }

        public LessonEntityBuilder startDatetime(LocalDateTime startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonEntityBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public LessonEntityBuilder roomId(Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public LessonEntityBuilder isDeleted(Boolean isDeleted) {
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

