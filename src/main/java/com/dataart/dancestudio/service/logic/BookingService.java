package com.dataart.dancestudio.service.logic;

import com.dataart.dancestudio.service.model.BookingDto;
import com.dataart.dancestudio.service.model.view.BookingViewDto;

import java.util.List;

public interface BookingService {

    void createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void updateBookingById(BookingDto bookingDto, int id);

    void deleteBookingById(int id);

    List<BookingViewDto> getAllBookings();

}
