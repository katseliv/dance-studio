package com.dataart.dancestudio.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {

    private Integer userId;

    private Integer lessonId;

    private Boolean isDeleted;

}
