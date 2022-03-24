package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    List<RoomViewDto> fromEntities(Iterable<RoomEntity> entities);

}
