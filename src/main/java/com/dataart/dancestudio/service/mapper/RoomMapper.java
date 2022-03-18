package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.RoomEntity;
import com.dataart.dancestudio.service.model.RoomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    List<RoomDto> fromEntities(Iterable<RoomEntity> entities);

}
