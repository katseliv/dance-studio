package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    List<RoomViewDto> roomEntitiesToRoomViewDtoList(Iterable<RoomEntity> entities);

}
