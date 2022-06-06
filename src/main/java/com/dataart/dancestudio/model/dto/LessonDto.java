package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.annotation.LessonStartDatetimeValid;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@LessonStartDatetimeValid
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@JsonDeserialize(builder = LessonDto.LessonDtoBuilder.class)
public class LessonDto {

    @NotNull(message = "Trainer is null.")
    @Positive(message = "Trainer Id is negative or zero.")
    private final Integer userTrainerId;

    @NotNull(message = "Dance Style is null.")
    @Positive(message = "Dance Style Id is negative or zero.")
    private final Integer danceStyleId;

    private final String startDatetime;

    @NotNull(message = "Duration is null.")
    @Min(value = 1, message = "Duration is too short.")
    @Max(value = 4, message = "Duration is too long.")
    private final Integer duration;

    @NotNull(message = "Room is null")
    @Positive(message = "Room Id is negative or zero.")
    private final Integer roomId;

    private final String timeZone;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LessonDtoBuilder {

    }

}
