package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
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

        if (bookingRepository.existsByUserIdAndLessonId(userId, lessonId)) {
            log.warn("Booking for userId = {} and lessonId = {} already exists!", userId, lessonId);
            throw new EntityAlreadyExistsException("Booking already exists!");
        }

        lessonRepository.findById(lessonId)
                .map(LessonEntity::getUserTrainer)
                .map(UserEntity::getId)
                .ifPresentOrElse(
                        (id) -> {
                            if (userId.equals(id)) {
                                log.warn("Booking userId = {} cannot be equal to lesson userTrainerId = {}", userId, id);
                                throw new EntityCreationException("User can't sign up for a lesson with himself!");
                            }
                        },
                        () -> {
                            log.warn("Lesson with id = {} doesn't exist", lessonId);
                            throw new EntityCreationException("Invalid lessonId. Can't create a booking!");
                        }
                );

        final BookingEntity bookingEntity = bookingRepository.save(bookingMapper.bookingDtoToBookingEntity(bookingDto));
        final int id = bookingEntity.getId();
        log.info("Booking with id = {} has been created", id);
        return id;
    }

    @Override
    public BookingDto getBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} has been found.", booking.getId()),
                () -> log.warn("Booking with id = {} hasn't been found.", id));
        return bookingEntity.map(bookingMapper::bookingEntityToBookingDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));
    }

    @Override
    public BookingViewDto getBookingViewById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} has been found.", booking.getId()),
                () -> log.warn("Booking with id = {} hasn't been found.", id));
        return bookingEntity.map(bookingMapper::bookingEntityToBookingViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));
    }

    @Override
    public void deleteBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        if (bookingEntity.isPresent()) {
            log.info("Booking with id = {} has been found.", id);
        } else {
            log.warn("Booking with id = {} hasn't been found.", id);
            throw new EntityNotFoundException("Booking not found!");
        }
        bookingRepository.markAsDeletedById(id);
        log.info("Booking with id = {} has been deleted.", id);
    }

    @Override
    public List<BookingViewDto> listBookings() {
        final List<BookingEntity> bookingEntities = bookingRepository.findAll();
        if (bookingEntities.size() != 0) {
            log.info("Bookings have been found.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    public List<BookingViewDto> listUserBookings(final int userId) {
        final List<BookingEntity> bookingEntities = bookingRepository.findAllByUserId(userId);
        if (bookingEntities.size() != 0) {
            log.info("Bookings have been found.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

}
