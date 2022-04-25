package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;

import java.util.List;

public interface BookingService {

    int createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void deleteBookingById(int id);

    List<BookingViewDto> listBookings();

    List<BookingViewDto> listUserBookings(int userId);

}
