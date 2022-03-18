package com.dataart.dancestudio.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class LessonDto {

    private Integer userTrainerId;

    private Integer danceStyleId;

    private Instant startDatetime;

    private Integer duration;

    private Integer roomId;

    private Boolean isDeleted;

}
