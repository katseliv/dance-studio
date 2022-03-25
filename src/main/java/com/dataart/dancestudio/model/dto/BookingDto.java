package com.dataart.dancestudio.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {

    private Integer userId;
    private Integer lessonId;
    private Boolean isDeleted;

}
