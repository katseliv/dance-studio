package com.dataart.dancestudio.service.model;

import com.dataart.dancestudio.db.entity.StudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    private String name;

    private String description;

    private StudioEntity studio;

    private Boolean isDeleted;

}
