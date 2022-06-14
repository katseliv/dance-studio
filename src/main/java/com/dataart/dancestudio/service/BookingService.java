package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    int createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void deleteBookingById(int id);

    ViewListPage<BookingViewDto> getViewListPage(String page, String size);

    ViewListPage<BookingViewDto> getUserViewListPage(int id, String page, String size);

    List<BookingViewDto> listBookings(Pageable pageable);

    int numberOfBookings();

    List<BookingViewDto> listUserBookings(int userId, Pageable pageable);

    int numberOfUserBookings(int userId);

    void findAndNotifyByStartingInHours(int hours);

}
