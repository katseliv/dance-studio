package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReviewEntity {

    private Integer id;
    private UserEntity userStudent;
    private UserEntity userTrainer;
    private String text;
    private Instant creationDatetime;
    private Integer rate;
    private boolean deleted;

}
