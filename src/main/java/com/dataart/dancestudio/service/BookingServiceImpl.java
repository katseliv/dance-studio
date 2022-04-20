package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        final int id = bookingEntity.getId();
        log.info(bookingEntity + " was created.");
        return id;
    }

    @Override
    public BookingDto getBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        if (bookingEntity.isPresent()) {
            log.info("Booking with id = " + bookingEntity.get().getId() + " was found.");
        } else {
            log.info("Booking wasn't found.");
        }
        return bookingMapper.bookingEntityToBookingDto(bookingEntity.orElseThrow());
    }

    @Override
    public BookingViewDto getBookingViewById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        if (bookingEntity.isPresent()) {
            log.info("Booking with id = " + bookingEntity.get().getId() + " was found.");
        } else {
            log.info("Booking wasn't found.");
        }
        return bookingMapper.bookingEntityToBookingViewDto(bookingEntity.orElseThrow());
    }

    @Override
    public void deleteBookingById(final int id) {
        bookingRepository.markAsDeletedById(id);
        log.info("Booking with id = " + id + " was deleted.");
    }

    @Override
    public List<BookingViewDto> listBookings() {
        final List<BookingEntity> bookingEntities = bookingRepository.findAll();
        if (bookingEntities.size() != 0) {
            log.info("Bookings were found.");
        } else {
            log.info("There aren't bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    public List<BookingViewDto> listUserBookings(final int userId) {
        final List<BookingEntity> bookingEntities = bookingRepository.findAllByUserId(userId);
        if (bookingEntities.size() != 0) {
            log.info("Bookings were found.");
        } else {
            log.info("There aren't bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

}
