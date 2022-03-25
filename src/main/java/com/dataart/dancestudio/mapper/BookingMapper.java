package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "lesson.id", source = "lessonId")
    BookingEntity bookingDtoToBookingEntity(BookingDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    BookingDto bookingEntityToBookingDto(BookingEntity entity);

    List<BookingDto> bookingEntitiesToBookingDtoList(Iterable<BookingEntity> entities);

}
