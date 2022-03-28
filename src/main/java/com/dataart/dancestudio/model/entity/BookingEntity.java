package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingEntity {

    private Integer id;
    private UserEntity user;
    private LessonEntity lesson;
    private Boolean isDeleted;

}
