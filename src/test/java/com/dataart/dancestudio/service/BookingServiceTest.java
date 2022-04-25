package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.BookingException;
import com.dataart.dancestudio.mapper.BookingMapperImpl;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import com.dataart.dancestudio.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Spy
    private BookingMapperImpl bookingMapperImpl;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private LessonRepository lessonRepositoryMock;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private final int id = 1;
    private final int userId = 13;
    private final int newUserId = 15;
    private final int lessonId = 14;
    private final boolean isDeleted = false;
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String danceStyle = "Popping";
    private final LocalDateTime startDatetime = LocalDateTime.now();
    private final BookingDto bookingDto = BookingDto.builder()
            .userId(userId)
            .lessonId(lessonId)
            .isDeleted(isDeleted)
            .build();
    final UserEntity userEntity = UserEntity.builder().id(userId).build();
    final UserEntity newUserEntity = UserEntity.builder().id(newUserId).build();
    final LessonEntity lessonEntity = LessonEntity.builder().id(lessonId).userTrainer(newUserEntity).build();
    private final BookingEntity bookingEntity = BookingEntity.builder()
            .id(id)
            .user(userEntity)
            .lesson(lessonEntity)
            .build();
    private final BookingViewDto bookingViewDto = BookingViewDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .danceStyle(danceStyle)
            .startDatetime(startDatetime)
            .build();

    @Test
    public void createBooking() throws BookingException {
        // given
        when(bookingMapperImpl.bookingDtoToBookingEntity(bookingDto)).thenReturn(bookingEntity);
        when(bookingRepositoryMock.save(bookingEntity)).thenReturn(bookingEntity);
        when(lessonRepositoryMock.findById(lessonId)).thenReturn(Optional.of(lessonEntity));

        // when
        final int bookingId = bookingServiceImpl.createBooking(bookingDto);

        // then
        verify(bookingRepositoryMock, times(1)).save(bookingEntity);
        assertEquals(id, bookingId);
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
        assertThrows(NoSuchElementException.class, () -> bookingServiceImpl.getBookingById(id));

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
        assertThrows(NoSuchElementException.class, () -> bookingServiceImpl.getBookingViewById(id));

        // then
        verify(bookingRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void deleteBookingById() {
        // given
        doNothing().when(bookingRepositoryMock).markAsDeletedById(id);

        // when
        bookingServiceImpl.deleteBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).markAsDeletedById(id);
    }

    @Test
    public void listBookings() {
        // given
        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingEntity> bookingEntities = List.of(bookingEntity);

        when(bookingMapperImpl.bookingEntitiesToBookingViewDtoList(bookingEntities)).thenReturn(bookingViewDtoListExpected);
        when(bookingRepositoryMock.findAll()).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listBookings();

        // then
        verify(bookingRepositoryMock, times(1)).findAll();
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void listUserBookings() {
        // given
        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingEntity> bookingEntities = List.of(bookingEntity);

        final int userId = 1;
        when(bookingMapperImpl.bookingEntitiesToBookingViewDtoList(bookingEntities)).thenReturn(bookingViewDtoListExpected);
        when(bookingRepositoryMock.findAllByUserId(userId)).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listUserBookings(userId);

        // then
        verify(bookingRepositoryMock, times(1)).findAllByUserId(userId);
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

}
