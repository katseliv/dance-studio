package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.BookingException;
import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
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

    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(final LessonRepository lessonRepository, final BookingRepository bookingRepository,
                              final BookingMapper bookingMapper) {
        this.lessonRepository = lessonRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public int createBooking(final BookingDto bookingDto) {
        final Integer userId = bookingDto.getUserId();
        final Integer lessonId = bookingDto.getLessonId();
        final LessonEntity lessonEntity = lessonRepository.findById(lessonId).orElseThrow();
        final Integer lessonUserId = lessonEntity.getUserTrainer().getId();
        if (!bookingRepository.existsByUserIdAndLessonId(userId, lessonId)) {
            if (!userId.equals(lessonUserId)) {
                final BookingEntity bookingEntity = bookingRepository.save(bookingMapper.bookingDtoToBookingEntity(bookingDto));
                final int id = bookingEntity.getId();
                log.info(bookingEntity + " was created.");
                return id;
            }
            log.info("Booking wasn't created.");
            throw new BookingException("User can't sign up for a lesson with himself!");
        }
        log.info("Booking wasn't created.");
        throw new BookingException("Booking already exists!");
    }

    @Override
    public BookingDto getBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} was found.", booking.getId()),
                () -> log.info("Booking wasn't found."));
        return bookingMapper.bookingEntityToBookingDto(bookingEntity.orElseThrow());
    }

    @Override
    public BookingViewDto getBookingViewById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} was found.", booking.getId()),
                () -> log.info("Booking wasn't found."));
        return bookingMapper.bookingEntityToBookingViewDto(bookingEntity.orElseThrow());
    }

    @Override
    public void deleteBookingById(final int id) {
        bookingRepository.markAsDeletedById(id);
        log.info("Booking with id = {} was deleted.", id);
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
