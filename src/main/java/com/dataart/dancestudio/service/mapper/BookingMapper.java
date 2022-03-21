package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.BookingEntity;
import com.dataart.dancestudio.service.model.BookingDto;
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
