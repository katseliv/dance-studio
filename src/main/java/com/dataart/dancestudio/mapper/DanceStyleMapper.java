package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DanceStyleMapper {

    List<DanceStyleViewDto> danceStyleEntitiesToDanceStyleViewDtoList(Iterable<DanceStyleEntity> entities);

}
