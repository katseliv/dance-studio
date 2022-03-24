package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DanceStyleMapper {

    List<DanceStyleViewDto> fromEntities(Iterable<DanceStyleEntity> entities);

}
