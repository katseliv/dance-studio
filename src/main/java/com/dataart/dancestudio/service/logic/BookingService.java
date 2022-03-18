package com.dataart.dancestudio.service.logic;

import com.dataart.dancestudio.service.model.BookingDto;

import java.util.List;

public interface BookingService {

    void createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    void updateBookingById(BookingDto bookingDto, int id);

    void deleteBookingById(int id);

    List<BookingDto> getAllBookings();

}
