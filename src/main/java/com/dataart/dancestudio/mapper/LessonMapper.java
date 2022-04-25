package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "userTrainerId", source = "userTrainer.id")
    @Mapping(target = "danceStyleId", source = "danceStyle.id")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "isDeleted", defaultValue = "false")
    LessonDto lessonEntityToLessonDto(LessonEntity entity);

    @Mapping(target = "trainerFirstName", source = "userTrainer.firstName")
    @Mapping(target = "trainerLastName", source = "userTrainer.lastName")
    @Mapping(target = "danceStyleName", source = "danceStyle.name")
    @Mapping(target = "startDatetime", source = "startDatetime")
    LessonViewDto lessonEntityToLessonViewDto(LessonEntity lessonEntity);

    @Mapping(target = "userTrainer.id", source = "userTrainerId")
    @Mapping(target = "danceStyle.id", source = "danceStyleId")
    @Mapping(target = "room.id", source = "roomId")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "startDatetime", source = ".", qualifiedByName = "startDatetime")
    LessonEntity lessonDtoToLessonEntity(LessonDto dto);

    @Named(value = "startDatetime")
    default LocalDateTime mapStartDatetime(final LessonDto lessonDto) {
        if (lessonDto.getTimeZone() == null) {
            return LocalDateTime.parse(lessonDto.getStartDatetime());
        }
        final LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());
        final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(lessonDto.getTimeZone()));
        final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    List<LessonViewDto> lessonEntitiesToLessonViewDtoList(Iterable<LessonEntity> entities);

}
