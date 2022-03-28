package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.db.entity.view.BookingViewEntity;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.service.model.view.BookingViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "lesson.id", source = "lessonId")
    BookingEntity bookingDtoToBookingEntity(BookingDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lessonId", source = "lesson.id")
    BookingDto bookingEntityToBookingDto(BookingEntity entity);

    BookingViewDto bookingViewEntityToBookingViewDto(BookingViewEntity entity);

    List<BookingViewDto> bookingViewEntitiesToBookingViewDtoList(Iterable<BookingViewEntity> entities);

}
