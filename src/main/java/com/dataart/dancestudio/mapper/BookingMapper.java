package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.dto.BookingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "lesson.id", source = "lessonId")
    BookingEntity toEntity(BookingDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    BookingDto fromEntity(BookingEntity entity);

    List<BookingDto> fromEntities(Iterable<BookingEntity> entities);

}
