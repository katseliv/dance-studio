package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.view.BookingViewEntity;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends Repository<BookingEntity> {

    Optional<BookingViewEntity> findViewById(int id);

    List<BookingViewEntity> findAllViews();

    List<BookingViewEntity> findAllUserBookingViews(int id);

}
