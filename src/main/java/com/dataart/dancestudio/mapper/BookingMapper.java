package com.dataart.dancestudio.mapper;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.view.BookingViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    BookingEntity bookingDtoToBookingEntity(BookingDto dto);

    @Mapping(target = "isDeleted", defaultValue = "false")
    BookingDto bookingEntityToBookingDto(BookingEntity entity);

    BookingViewDto bookingViewEntityToBookingViewDto(BookingViewEntity entity);

    List<BookingViewDto> bookingViewEntitiesToBookingViewDtoList(Iterable<BookingViewEntity> entities);

}
