package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
    default LocalDateTime mapStartDatetimeToEntity(final LessonDto lessonDto) {
        final LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());
        final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(lessonDto.getTimeZone()));
        final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetimeFromEntity")
    LessonDto fromEntity(LessonEntity entity);

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetimeFromEntity")
    LessonViewDto fromEntity(LessonViewEntity entity);

    @Named(value = "startDatetimeFromEntity")
    default LocalDateTime mapStartDatetimeFromEntity(final LocalDateTime startDatetime) {
        final ZonedDateTime utcZoned = startDatetime.atZone(ZoneId.of("UTC"));
        final ZonedDateTime localDateTimeZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());
        return localDateTimeZoned.toLocalDateTime();
    }

    List<LessonViewDto> fromEntities(Iterable<LessonViewEntity> entities);

}
