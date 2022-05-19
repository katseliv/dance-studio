package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.DatetimeValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@DatetimeValid
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = LessonDto.LessonDtoBuilder.class)
public class LessonDto {

    @NotNull(message = "Trainer is null.")
    private final Integer userTrainerId;

    @NotNull(message = "Dance Style is null.")
    private final Integer danceStyleId;

    @NotBlank(message = "Start Datetime is null.")
    private final String startDatetime;

    @NotNull(message = "Duration is null.")
    @Min(value = 1, message = "Duration is too short.")
    @Max(value = 4, message = "Duration is too long.")
    private final Integer duration;

    @NotNull(message = "Room time is null")
    private final Integer roomId;

    private final String timeZone;

    public static LessonDtoBuilder builder() {
        return new LessonDtoBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class LessonDtoBuilder {

        private Integer userTrainerId;
        private Integer danceStyleId;
        private String startDatetime;
        private Integer duration;
        private Integer roomId;
        private String timeZone;

        private boolean isUsed = false;

        public LessonDtoBuilder() {
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

        public LessonDtoBuilder duration(final Integer duration) {
            this.duration = duration;
            return this;
        }

        public LessonDtoBuilder roomId(final Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public LessonDtoBuilder timeZone(final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public LessonDto build() {
            if (!isUsed) {
                isUsed = true;
                return new LessonDto(userTrainerId, danceStyleId, startDatetime, duration, roomId, timeZone);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
