package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;

public interface BookingService {

    int createBooking(BookingDto bookingDto);

    BookingDto getBookingById(int id);

    BookingViewDto getBookingViewById(int id);

    void deleteBookingById(int id);

    ViewListPage<BookingViewDto> getViewListPage(String page, String size);

    ViewListPage<BookingViewDto> getUserViewListPage(int id, String page, String size);

}
