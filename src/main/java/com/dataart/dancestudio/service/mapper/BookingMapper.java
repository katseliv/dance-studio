package com.dataart.dancestudio.service.mapper;

import com.dataart.dancestudio.db.entity.BookingEntity;
import com.dataart.dancestudio.db.entity.view.BookingViewEntity;
import com.dataart.dancestudio.service.model.BookingDto;
import com.dataart.dancestudio.service.model.view.BookingViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "isDeleted", defaultValue = "false")
    BookingEntity toEntity(BookingDto dto);

    BookingDto fromEntity(BookingEntity entity);

    BookingViewDto fromEntity(BookingViewEntity entity);

    List<BookingViewDto> fromEntities(Iterable<BookingViewEntity> entities);

}
