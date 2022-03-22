package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.RoomEntity;
import com.dataart.dancestudio.service.model.view.RoomViewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    List<RoomViewDto> fromEntities(Iterable<RoomEntity> entities);

}
