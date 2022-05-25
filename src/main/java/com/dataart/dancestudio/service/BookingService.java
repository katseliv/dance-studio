package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;

public interface BookingService {

    int createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void deleteBookingById(int id);

}
