package com.dataart.dancestudio.db.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class LessonEntity {

    private Integer id;

    private UserEntity userTrainer;

    private DanceStyleEntity danceStyle;

    private Instant startDatetime;

    private Integer duration;

    private RoomEntity room;

    private Boolean isDeleted;

}
