package com.dataart.dancestudio.db.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PostEntity {

    private Integer id;

    private UserEntity userAdmin;

    private String image;

    private String text;

    private Instant creationDatetime;

    private Boolean isDeleted;

}
