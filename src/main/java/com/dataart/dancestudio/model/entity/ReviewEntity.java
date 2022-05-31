package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class ReviewEntity {

    private Integer id;
    private UserEntity userStudent;
    private UserEntity userTrainer;
    private String text;
    private Instant creationDatetime;
    private Integer rate;
    private boolean deleted;

}
