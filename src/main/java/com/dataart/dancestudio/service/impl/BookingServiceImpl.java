package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.repository.impl.BookingRepository;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
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
        return bookingRepository.save(bookingMapper.toEntity(bookingDto));
    }

    @Override
    public BookingDto getBookingById(final int id) {
        return bookingMapper.fromEntity(bookingRepository.findById(id).orElseThrow());
    }

    @Override
    public void updateBookingById(final BookingDto bookingDto, final int id) {
        bookingRepository.update(bookingMapper.toEntity(bookingDto), id);
    }

    @Override
    public void deleteBookingById(final int id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingMapper.fromEntities(bookingRepository.findAll());
    }

}
