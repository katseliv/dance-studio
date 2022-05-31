package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class PostEntity {

    private Integer id;
    private UserEntity userAdmin;
    private String image;
    private String text;
    private Instant creationDatetime;
    private boolean deleted;

}
