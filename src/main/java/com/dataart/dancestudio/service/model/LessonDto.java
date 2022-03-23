package com.dataart.dancestudio.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LessonDto {

    private final Integer id;

    private final Integer userTrainerId;

    private final Integer danceStyleId;

    private final String startDatetime;

    private final Integer duration;

    private final Integer roomId;

    private final Boolean isDeleted;

    private final String timeZone;

    public static LessonDtoBuilder builder() {
        return new LessonDtoBuilder();
    }

    public static class LessonDtoBuilder {

        private Integer id;

        private Integer userTrainerId;

        private Integer danceStyleId;

        private String startDatetime;

        private Integer duration;

        private Integer roomId;

        private Boolean isDeleted;

        private String timeZone;

        private boolean isUsed = false;

        public LessonDtoBuilder() {
        }

        public LessonDtoBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public LessonDtoBuilder userTrainerId(Integer userTrainerId) {
            this.userTrainerId = userTrainerId;
            return this;
        }

        public LessonDtoBuilder danceStyleId(Integer danceStyleId) {
            this.danceStyleId = danceStyleId;
            return this;
        }

        public LessonDtoBuilder startDatetime(String startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonDtoBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public LessonDtoBuilder roomId(Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public LessonDtoBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public LessonDtoBuilder timeZone(String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public LessonDto build() {
            if (!isUsed) {
                isUsed = true;
                return new LessonDto(id, userTrainerId, danceStyleId, startDatetime, duration, roomId, isDeleted, timeZone);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
