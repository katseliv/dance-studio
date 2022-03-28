package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.service.model.view.BookingViewDto;

import java.util.List;

public interface BookingService {

    int createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void updateBookingById(BookingDto bookingDto, int id);

    void deleteBookingById(int id);

    List<BookingDto> listBookings();

}
