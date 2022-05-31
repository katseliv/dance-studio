package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class TrainerProfileEntity {

    private Integer id;
    private UserEntity userTrainer;
    private Instant experiencedSince;
    private String description;
    private Integer salary;

}
