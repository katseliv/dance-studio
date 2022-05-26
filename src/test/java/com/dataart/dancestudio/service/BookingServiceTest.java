package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.BookingMapperImpl;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Spy
    private BookingMapperImpl bookingMapperImpl;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private LessonRepository lessonRepositoryMock;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private final int id = 1;
    private final int userId = 13;
    private final int newUserId = 15;
    private final int lessonId = 14;
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String danceStyle = "Popping";
    private final LocalDateTime startDatetime = LocalDateTime.now();
    private final BookingDto bookingDto = BookingDto.builder()
            .userId(userId)
            .lessonId(lessonId)
            .build();
    final UserEntity userEntity = UserEntity.builder()
            .id(userId)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    final DanceStyleEntity danceStyleEntity = DanceStyleEntity.builder()
            .id(userId)
            .name(danceStyle)
            .build();
    final UserEntity newUserEntity = UserEntity.builder()
            .id(newUserId)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    final LessonEntity lessonEntity = LessonEntity.builder()
            .id(lessonId)
            .userTrainer(newUserEntity)
            .danceStyle(danceStyleEntity)
            .startDatetime(startDatetime)
            .build();
    private final BookingEntity bookingEntity = BookingEntity.builder()
            .id(id)
            .user(userEntity)
            .lesson(lessonEntity)
            .build();
    private final BookingViewDto bookingViewDto = BookingViewDto.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .danceStyle(danceStyle)
            .startDatetime(startDatetime)
            .build();

    @Test
    public void createBooking() {
        // given
        when(bookingMapperImpl.bookingDtoToBookingEntity(bookingDto)).thenReturn(bookingEntity);
        when(bookingRepositoryMock.save(bookingEntity)).thenReturn(bookingEntity);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userEntity));
        when(lessonRepositoryMock.findById(lessonId)).thenReturn(Optional.of(lessonEntity));

        // when
        final int bookingId = bookingServiceImpl.createBooking(bookingDto);

        // then
        verify(bookingRepositoryMock, times(1)).save(bookingEntity);
        assertEquals(id, bookingId);
    }

    @Test
    public void createBookingWhenBookingAlreadyExists() {
        // given
        when(bookingRepositoryMock.existsByUserIdAndLessonId(userId, lessonId)).thenReturn(true);

        // when then
        final var actualException = assertThrowsExactly(EntityAlreadyExistsException.class,
                () -> bookingServiceImpl.createBooking(bookingDto));
        verify(bookingRepositoryMock, never()).save(bookingEntity);
        assertEquals(actualException.getMessage(), "Booking already exists!");
    }

    @Test
    public void createBookingWhenUserNotFound() {
        // given
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> bookingServiceImpl.createBooking(bookingDto));
        verify(bookingRepositoryMock, never()).save(bookingEntity);
        assertEquals(actualException.getMessage(), "Invalid userId. Can't create a booking!");
    }

    @Test
    public void createBookingWhenLessonNotFound() {
        // given
        when(lessonRepositoryMock.findById(lessonId)).thenReturn(Optional.empty());
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.ofNullable(userEntity));

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> bookingServiceImpl.createBooking(bookingDto));
        verify(bookingRepositoryMock, never()).save(bookingEntity);
        assertEquals(actualException.getMessage(), "Invalid lessonId. Can't create a booking!");
    }

    @Test
    public void createBookingWhenTheSameUserTrainerId() {
        // given
        final var build = LessonEntity.builder()
                .userTrainer(UserEntity.builder().id(userId).build())
                .build();
        when(lessonRepositoryMock.findById(lessonId)).thenReturn(Optional.of(build));
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userEntity));

        // when then
        final var actualException = assertThrowsExactly(EntityCreationException.class,
                () -> bookingServiceImpl.createBooking(bookingDto));
        verify(bookingRepositoryMock, never()).save(bookingEntity);
        assertEquals(actualException.getMessage(), "User can't sign up for a lesson with himself!");
    }

    @Test
    public void getBookingById() {
        // given
        when(bookingMapperImpl.bookingEntityToBookingDto(bookingEntity)).thenReturn(bookingDto);
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));

        // when
        final BookingDto bookingDtoActual = bookingServiceImpl.getBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).findById(id);
        assertEquals(bookingDto, bookingDtoActual);
    }

    @Test
    public void getBookingByIdWhenOptionalNull() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> bookingServiceImpl.getBookingById(id));

        // then
        verify(bookingMapperImpl, never()).bookingEntityToBookingDto(bookingEntity);
        verify(bookingRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void getBookingViewById() {
        // given
        when(bookingMapperImpl.bookingEntityToBookingViewDto(bookingEntity)).thenReturn(bookingViewDto);
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));

        // when
        final BookingViewDto bookingViewDtoActual = bookingServiceImpl.getBookingViewById(id);

        // then
        verify(bookingRepositoryMock, times(1)).findById(id);
        assertEquals(bookingViewDto, bookingViewDtoActual);
    }

    @Test
    public void getBookingViewByIdWhenOptionalNull() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> bookingServiceImpl.getBookingViewById(id));

        // then
        verify(bookingRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void deleteBookingById() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(bookingEntity));
        doNothing().when(bookingRepositoryMock).markAsDeletedById(id);

        // when
        bookingServiceImpl.deleteBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).markAsDeletedById(id);
    }

    @Test
    public void deleteBookingByIdWhenBookingDoesNotExist() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityNotFoundException.class,
                () -> bookingServiceImpl.deleteBookingById(id));
        verify(bookingRepositoryMock, never()).markAsDeletedById(id);
        assertEquals(actualException.getMessage(), "Booking not found!");
    }

    @Test
    public void listBookings() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final Page<BookingEntity> bookingEntities = new PageImpl<>(List.of(bookingEntity));
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(bookingRepositoryMock.findAll(eq(pageable))).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listEntities(pageable);

        // then
        verify(bookingRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void emptyListBookings() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<BookingViewDto> bookingViewDtoListExpected = new ArrayList<>();
        final Page<BookingEntity> bookingEntities = new PageImpl<>(new ArrayList<>());
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(bookingRepositoryMock.findAll(eq(pageable))).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listEntities(pageable);

        // then
        verify(bookingRepositoryMock, times(1)).findAll(eq(pageable));
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void listUserBookings() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingEntity> bookingEntities = List.of(bookingEntity);
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final int userId = 1;
        when(bookingMapperImpl.bookingEntitiesToBookingViewDtoList(bookingEntities)).thenReturn(bookingViewDtoListExpected);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.ofNullable(UserEntity.builder().build()));
        when(bookingRepositoryMock.findAllByUserId(userId, pageable)).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listUserEntities(userId, pageable);

        // then
        verify(bookingRepositoryMock, times(1)).findAllByUserId(userId, pageable);
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void listUserBookingsWhenUserDoesNotExist() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final int userId = 1;
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // when then
        final var actualException = assertThrowsExactly(EntityNotFoundException.class,
                () -> bookingServiceImpl.listUserEntities(userId, pageable));
        verify(bookingRepositoryMock, never()).findAllByUserId(userId, pageable);
        assertEquals(actualException.getMessage(), "User not found!");
    }

    @Test
    public void emptyListUserBookings() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<BookingViewDto> bookingViewDtoListExpected = new ArrayList<>();
        final List<BookingEntity> bookingEntities = new ArrayList<>();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final int userId = 1;
        when(bookingMapperImpl.bookingEntitiesToBookingViewDtoList(bookingEntities)).thenReturn(bookingViewDtoListExpected);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.ofNullable(UserEntity.builder().build()));
        when(bookingRepositoryMock.findAllByUserId(userId, pageable)).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listUserEntities(userId, pageable);

        // then
        verify(bookingRepositoryMock, times(1)).findAllByUserId(userId, pageable);
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void numberOfEntities() {
        // given
        final int amountExpected = 5;

        when(bookingRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = bookingServiceImpl.numberOfEntities();

        // then
        verify(bookingRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void zeroNumberOfEntities() {
        // given
        final int amountExpected = 0;

        when(bookingRepositoryMock.count()).thenReturn((long) amountExpected);

        // when
        final int amountActual = bookingServiceImpl.numberOfEntities();

        // then
        verify(bookingRepositoryMock, times(1)).count();
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void numberOfUserEntities() {
        // given
        final int amountExpected = 5;

        when(bookingRepositoryMock.countAllByUserId(userId)).thenReturn(amountExpected);

        // when
        final int amountActual = bookingServiceImpl.numberOfUserEntities(userId);

        // then
        verify(bookingRepositoryMock, times(1)).countAllByUserId(userId);
        assertEquals(amountExpected, amountActual);
    }

    @Test
    public void zeroNumberOfUserEntities() {
        // given
        final int amountExpected = 0;

        when(bookingRepositoryMock.countAllByUserId(userId)).thenReturn(amountExpected);

        // when
        final int amountActual = bookingServiceImpl.numberOfUserEntities(userId);

        // then
        verify(bookingRepositoryMock, times(1)).countAllByUserId(userId);
        assertEquals(amountExpected, amountActual);
    }

}
