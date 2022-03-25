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
    @Mapping(target = "startDatetime", source = ".", qualifiedByName = "startDatetime")
    LessonEntity lessonDtoToLessonEntity(LessonDto dto);

    @Named(value = "startDatetime")
    default LocalDateTime mapStartDatetime(final LessonDto lessonDto) {
        final LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());
        final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(lessonDto.getTimeZone()));
        final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    LessonDto lessonEntityToLessonDto(LessonEntity entity);

    LessonViewDto lessonViewEntityToLessonViewDto(LessonViewEntity entity);

    List<LessonViewDto> lessonViewEntitiesToLessonViewDtoList(Iterable<LessonViewEntity> entities);

}
