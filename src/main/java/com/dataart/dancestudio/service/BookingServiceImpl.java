package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
        final BookingEntity bookingEntity = bookingRepository.save(bookingMapper.bookingDtoToBookingEntity(bookingDto));
        return bookingEntity.getId();
    }

    @Override
    public BookingDto getBookingById(final int id) {
        return bookingMapper.bookingEntityToBookingDto(bookingRepository.findById(id).orElseThrow());
    }

    @Override
    public BookingViewDto getBookingViewById(final int id) {
        return bookingMapper.bookingEntityToBookingViewDto(bookingRepository.findById(id).orElseThrow());
    }

    @Override
    public void deleteBookingById(final int id) {
        bookingRepository.markAsDeletedById(id);
    }

    @Override
    public List<BookingViewDto> listBookings() {
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingRepository.findAll());
    }

    @Override
    public List<BookingViewDto> listUserBookings(final int userId) {
        return bookingMapper.bookingEntitiesToBookingViewDtoList(
                bookingRepository.findAllByUserId(userId));
    }

}
