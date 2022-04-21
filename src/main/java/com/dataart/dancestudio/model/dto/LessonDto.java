package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class LessonDto {

    private final Integer id;

    @NotNull(message = "Trainer is null.")
    private final Integer userTrainerId;

    @NotNull(message = "Dance Style is null.")
    private final Integer danceStyleId;

    @NotBlank(message = "Start Datetime is null.")
    private final String startDatetime;

    @NotNull(message = "Duration is null.")
    @Pattern(regexp = "-?[0-9]+", message = "Duration invalid.")
    @Min(value = 1, message = "Duration is too short.")
    @Max(value = 4, message = "Duration is too long.")
    private final String duration;

    @NotNull(message = "Room time is null")
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
        private String duration;
        private Integer roomId;
        private Boolean isDeleted;
        private String timeZone;

        private boolean isUsed = false;

        public LessonDtoBuilder() {
        }

        public LessonDtoBuilder id(final Integer id) {
            this.id = id;
            return this;
        }

        public LessonDtoBuilder userTrainerId(final Integer userTrainerId) {
            this.userTrainerId = userTrainerId;
            return this;
        }

        public LessonDtoBuilder danceStyleId(final Integer danceStyleId) {
            this.danceStyleId = danceStyleId;
            return this;
        }

        public LessonDtoBuilder startDatetime(final String startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LessonDtoBuilder duration(final String duration) {
            this.duration = duration;
            return this;
        }

        public LessonDtoBuilder roomId(final Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public LessonDtoBuilder isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public LessonDtoBuilder timeZone(final String timeZone) {
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
