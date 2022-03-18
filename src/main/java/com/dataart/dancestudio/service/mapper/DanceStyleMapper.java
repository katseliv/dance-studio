package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.DanceStyleEntity;
import com.dataart.dancestudio.service.model.DanceStyleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DanceStyleMapper {

    List<DanceStyleDto> fromEntities(Iterable<DanceStyleEntity> entities);

}
