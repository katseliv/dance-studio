package com.dataart.dancestudio.db.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomEntity {

    private Integer id;

    private String name;

    private String description;

    private StudioEntity studio;

    private Boolean isDeleted;

}
