package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TrainerProfileEntity {

    private Integer id;
    private UserEntity userTrainer;
    private Instant experiencedSince;
    private String description;
    private Integer salary;

}
