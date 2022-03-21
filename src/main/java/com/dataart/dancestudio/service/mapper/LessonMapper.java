package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.LessonEntity;
import com.dataart.dancestudio.service.model.LessonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "userTrainer.id", source = "userTrainerId")
    @Mapping(target = "danceStyle.id", source = "danceStyleId")
    @Mapping(target = "room.id", source = "roomId")
    LessonEntity toEntity(LessonDto dto);

    @Mapping(target = "userTrainerId", source = "userTrainer.id")
    @Mapping(target = "danceStyleId", source = "danceStyle.id")
    @Mapping(target = "roomId", source = "room.id")
    LessonDto fromEntity(LessonEntity entity);

    List<LessonDto> fromEntities(Iterable<LessonEntity> entities);

}
