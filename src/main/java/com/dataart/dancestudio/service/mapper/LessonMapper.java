package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.LessonEntity;
import com.dataart.dancestudio.db.entity.view.LessonViewEntity;
import com.dataart.dancestudio.service.model.LessonDto;
import com.dataart.dancestudio.service.model.view.LessonViewDto;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "startDatetime", source = ".", qualifiedByName = "startDatetimeToEntity")
    LessonEntity toEntity(LessonDto dto);

    @Named(value = "startDatetimeToEntity")
    default LocalDateTime mapStartDatetimeToEntity(LessonDto lessonDto) {
        LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());
        ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(lessonDto.getTimeZone()));
        ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetimeFromEntity")
    LessonDto fromEntity(LessonEntity entity);

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetimeFromEntity")
    LessonViewDto fromEntity(LessonViewEntity entity);

    @Named(value = "startDatetimeFromEntity")
    default LocalDateTime mapStartDatetimeFromEntity(LocalDateTime startDatetime) {
        ZonedDateTime utcZoned = startDatetime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localDateTimeZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localDateTimeZoned.toLocalDateTime();
    }

    List<LessonViewDto> fromEntities(Iterable<LessonViewEntity> entities);

}
