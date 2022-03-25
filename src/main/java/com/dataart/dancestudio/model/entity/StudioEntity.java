package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StudioEntity {

    private Integer id;
    private String name;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;

}
