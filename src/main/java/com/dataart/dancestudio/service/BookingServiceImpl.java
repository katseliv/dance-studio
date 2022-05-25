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
import com.dataart.dancestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService, EntityService<BookingViewDto>, UserEntityService<BookingViewDto> {

    private final BookingRepository bookingRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public int createBooking(final BookingDto bookingDto) {
        final Integer userId = bookingDto.getUserId();
        final Integer lessonId = bookingDto.getLessonId();

        if (bookingRepository.existsByUserIdAndLessonId(userId, lessonId)) {
            log.warn("Booking for userId = {} and lessonId = {} already exists!", userId, lessonId);
            throw new EntityAlreadyExistsException("Booking already exists!");
        }

        userRepository.findById(userId).ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", userId),
                () -> {
                    log.warn("User with id = {} hasn't been found.", userId);
                    throw new EntityCreationException("Invalid userId. Can't create a booking!");
                }
        );
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

        final BookingEntity bookingEntity = Optional.of(bookingDto)
                .map(bookingMapper::bookingDtoToBookingEntity)
                .map(bookingRepository::save)
                .orElseThrow(() -> new EntityCreationException("Booking not created!"));
        log.info("Booking with id = {} has been created", bookingEntity.getId());
        return bookingEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} has been found.", id),
                () -> log.warn("Booking with id = {} hasn't been found.", id));
        return bookingEntity.map(bookingMapper::bookingEntityToBookingDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingViewDto getBookingViewById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} has been found.", id),
                () -> log.warn("Booking with id = {} hasn't been found.", id));
        return bookingEntity.map(bookingMapper::bookingEntityToBookingViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));
    }

    @Override
    @Transactional
    public void deleteBookingById(final int id) {
        final Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        bookingEntity.ifPresentOrElse(
                (booking) -> log.info("Booking with id = {} has been found.", id),
                () -> {
                    log.warn("Booking with id = {} hasn't been found.", id);
                    throw new EntityNotFoundException("Booking not found!");
                });
        bookingRepository.markAsDeletedById(id);
        log.info("Booking with id = {} has been deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingViewDto> listEntities(final Pageable pageable) {
        final List<BookingEntity> bookingEntities = bookingRepository.findAll(pageable).getContent();
        if (bookingEntities.size() != 0) {
            log.info("Bookings have been found.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfEntities() {
        final long numberOfBookings = bookingRepository.count();
        if (numberOfBookings != 0) {
            log.info("There have been bookings.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return (int) numberOfBookings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingViewDto> listUserEntities(final int userId, final Pageable pageable) {
        final Optional<UserEntity> userEntity = userRepository.findById(userId);
        userEntity.ifPresentOrElse(
                (booking) -> log.info("User with id = {} has been found.", userId),
                () -> {
                    log.warn("User with id = {} hasn't been found.", userId);
                    throw new EntityNotFoundException("User not found!");
                });

        final List<BookingEntity> bookingEntities = bookingRepository.findAllByUserId(userId, pageable);
        if (bookingEntities.size() != 0) {
            log.info("Bookings have been found.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfUserEntities(final int userId) {
        final int numberOfUserBookings = bookingRepository.countAllByUserId(userId);
        if (numberOfUserBookings != 0) {
            log.info("There have been bookings.");
        } else {
            log.warn("There haven't been bookings.");
        }
        return numberOfUserBookings;
    }

}
