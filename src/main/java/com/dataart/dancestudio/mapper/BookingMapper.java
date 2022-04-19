package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "isDeleted", defaultValue = "false")
    BookingDto bookingEntityToBookingDto(BookingEntity entity);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "danceStyle", source = "lesson.danceStyle.name")
    @Mapping(target = "startDatetime", source = "lesson.startDatetime")
    BookingViewDto bookingEntityToBookingViewDto(BookingEntity entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "lesson.id", source = "lessonId")
    @Mapping(target = "isDeleted", defaultValue = "false")
    BookingEntity bookingDtoToBookingEntity(BookingDto dto);

    List<BookingViewDto> bookingEntitiesToBookingViewDtoList(Iterable<BookingEntity> entities);

}
