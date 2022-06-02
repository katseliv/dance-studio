package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.BookingMapper;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import com.dataart.dancestudio.utils.ParseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService, PaginationService<BookingViewDto> {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;
    @Value("${pagination.buttonLimit}")
    private int buttonLimit;

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
    public List<BookingViewDto> listBookings(final Pageable pageable) {
        final List<BookingEntity> bookingEntities = bookingRepository.findAll(pageable).getContent();
        log.info("There have been found {} bookings.", bookingEntities.size());
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfBookings() {
        final long numberOfBookings = bookingRepository.count();
        log.info("There have been found {} bookings.", numberOfBookings);
        return (int) numberOfBookings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingViewDto> listUserBookings(final int userId, final Pageable pageable) {
        final Optional<UserEntity> userEntity = userRepository.findById(userId);
        userEntity.ifPresentOrElse(
                (booking) -> log.info("User with id = {} has been found.", userId),
                () -> {
                    log.warn("User with id = {} hasn't been found.", userId);
                    throw new EntityNotFoundException("User not found!");
                });

        final List<BookingEntity> bookingEntities = bookingRepository.findAllByUserId(userId, pageable);
        log.info("There have been found {} bookings for userId {}.", bookingEntities.size(), userId);
        return bookingMapper.bookingEntitiesToBookingViewDtoList(bookingEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfUserBookings(final int userId) {
        final int numberOfUserBookings = bookingRepository.countAllByUserId(userId);
        log.info("There have been found {} bookings.", numberOfUserBookings);
        return numberOfUserBookings;
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<BookingViewDto> getViewListPage(final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<BookingViewDto> listBookings = listBookings(pageable);
        final int totalAmount = numberOfBookings();

        return getViewListPage(totalAmount, pageSize, pageNumber, listBookings);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<BookingViewDto> getUserViewListPage(final int id, final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<BookingViewDto> listUserBookings = listUserBookings(id, pageable);
        final int totalAmount = numberOfUserBookings(id);

        return getViewListPage(totalAmount, pageSize, pageNumber, listUserBookings);
    }

    @Override
    public int getButtonLimit() {
        return buttonLimit;
    }

}
