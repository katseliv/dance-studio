package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "studioId", source = "studio.id")
    RoomViewDto roomEntityToRoomViewDto(RoomEntity roomEntity);

    List<RoomViewDto> roomEntitiesToRoomViewDtoList(Iterable<RoomEntity> entities);

}
