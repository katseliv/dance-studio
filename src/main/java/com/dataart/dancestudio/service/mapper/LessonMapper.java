package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.LessonEntity;
import com.dataart.dancestudio.db.entity.view.LessonViewEntity;
import com.dataart.dancestudio.service.model.LessonDto;
import com.dataart.dancestudio.service.model.view.LessonViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetime")
    LessonEntity toEntity(LessonDto dto);

    @Named(value = "startDatetime")
    default LocalDateTime mapStartDatetime(String startDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(startDatetime, formatter);
        ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZoned.toLocalDateTime();
    }

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetime")
    LessonDto fromEntity(LessonEntity entity);

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "startDatetime")
    LessonViewDto fromEntity(LessonViewEntity entity);

    @Named(value = "startDatetime")
    default LocalDateTime mapStartDatetime(LocalDateTime startDatetime) {

        ZonedDateTime utcZoned = startDatetime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localDateTimeZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault());

        return localDateTimeZoned.toLocalDateTime();
    }

    List<LessonViewDto> fromEntities(Iterable<LessonViewEntity> entities);

}
