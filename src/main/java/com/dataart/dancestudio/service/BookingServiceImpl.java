package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(final BookingRepository bookingRepository, final BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public int createBooking(final BookingDto bookingDto) {
        return bookingRepository.save(bookingMapper.bookingDtoToBookingEntity(bookingDto));
    }

    @Override
    public BookingDto getBookingById(final int id) {
        return bookingMapper.bookingEntityToBookingDto(bookingRepository.findById(id).orElseThrow());
    }

    @Override
    public BookingViewDto getBookingViewById(final int id) {
        return bookingMapper.bookingViewEntityToBookingViewDto(bookingRepository.findViewById(id).orElseThrow());
    }

    @Override
    public void updateBookingById(final BookingDto bookingDto, final int id) {
        final BookingDto bookingDtoFromDB = getBookingById(id);
        if (!bookingDto.equals(bookingDtoFromDB)) {
            bookingRepository.update(bookingMapper.bookingDtoToBookingEntity(bookingDto), id);
        }
    }

    @Override
    public void deleteBookingById(final int id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingViewDto> listBookings() {
        return bookingMapper.bookingViewEntitiesToBookingViewDtoList(bookingRepository.findAllViews());
    }

    @Override
    public List<BookingViewDto> listUserBookings(final int id) {
        return bookingMapper.bookingViewEntitiesToBookingViewDtoList(bookingRepository.findAllUserBookingViews(id));
    }

}
