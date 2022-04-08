package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.BookingMapperImpl;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.view.BookingViewEntity;
import com.dataart.dancestudio.repository.BookingRepository;
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

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private final int id = 1;
    private final int userId = 13;
    private final int lessonId = 14;
    private final int newUserId = 2300;
    private final int newLessonId = 1400;
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
    private final BookingEntity bookingEntity = BookingEntity.builder()
            .userId(userId)
            .lessonId(lessonId)
            .isDeleted(isDeleted)
            .build();
    private final BookingViewDto bookingViewDto = BookingViewDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .danceStyle(danceStyle)
            .startDatetime(startDatetime)
            .build();
    private final BookingViewEntity bookingViewEntity = BookingViewEntity.builder()
            .firstName(firstName)
            .lastName(lastName)
            .danceStyle(danceStyle)
            .startDatetime(startDatetime)
            .build();
    private final BookingDto newBookingDto = BookingDto.builder()
            .userId(newUserId)
            .lessonId(newLessonId)
            .isDeleted(isDeleted)
            .build();
    private final BookingEntity newBookingEntity = BookingEntity.builder()
            .userId(newUserId)
            .lessonId(newLessonId)
            .isDeleted(isDeleted)
            .build();

    @Test
    public void createBooking() {
        // given
        when(bookingRepositoryMock.save(bookingEntity)).thenReturn(id);

        // when
        final int bookingId = bookingServiceImpl.createBooking(bookingDto);

        // then
        verify(bookingMapperImpl, times(1)).bookingDtoToBookingEntity(bookingDto);
        assertEquals(id, bookingId);
    }

    @Test
    public void getBookingById() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));

        // when
        final BookingDto bookingDtoActual = bookingServiceImpl.getBookingById(id);

        // then
        verify(bookingMapperImpl, times(1)).bookingEntityToBookingDto(bookingEntity);
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
        when(bookingRepositoryMock.findViewById(id)).thenReturn(Optional.of(bookingViewEntity));

        // when
        final BookingViewDto bookingViewDtoActual = bookingServiceImpl.getBookingViewById(id);

        // then
        verify(bookingMapperImpl, times(1)).bookingViewEntityToBookingViewDto(bookingViewEntity);
        verify(bookingRepositoryMock, times(1)).findViewById(id);
        assertEquals(bookingViewDto, bookingViewDtoActual);
    }

    @Test
    public void getBookingViewByIdWhenOptionalNull() {
        // given
        when(bookingRepositoryMock.findViewById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> bookingServiceImpl.getBookingViewById(id));

        // then
        verify(bookingMapperImpl, never()).bookingViewEntityToBookingViewDto(bookingViewEntity);
        verify(bookingRepositoryMock, times(1)).findViewById(id);
    }

    @Test
    public void updateBookingById() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));
        doNothing().when(bookingRepositoryMock).update(newBookingEntity, id);

        // when
        bookingServiceImpl.updateBookingById(newBookingDto, id);

        // then
        verify(bookingRepositoryMock, times(1)).update(newBookingEntity, id);
        verify(bookingMapperImpl, times(1)).bookingDtoToBookingEntity(newBookingDto);
    }

    @Test
    public void updateBookingByIdWhenOptionalNull() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> bookingServiceImpl.updateBookingById(newBookingDto, id));

        // then
        verify(bookingRepositoryMock, never()).update(newBookingEntity, id);
        verify(bookingMapperImpl, never()).bookingDtoToBookingEntity(newBookingDto);
    }

    @Test
    public void doesNotUpdateBookingById() {
        // given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));

        // when
        bookingServiceImpl.updateBookingById(bookingDto, id);

        // then
        verify(bookingRepositoryMock, never()).update(newBookingEntity, id);
        verify(bookingMapperImpl, never()).bookingDtoToBookingEntity(bookingDto);
    }

    @Test
    public void doesNotUpdateBookingByIdWhenOptionalNull() {
        // given;
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> bookingServiceImpl.updateBookingById(bookingDto, id));

        // then
        verify(bookingRepositoryMock, never()).update(newBookingEntity, id);
        verify(bookingMapperImpl, never()).bookingDtoToBookingEntity(bookingDto);
    }

    @Test
    public void deleteBookingById() {
        // given
        doNothing().when(bookingRepositoryMock).deleteById(id);

        // when
        bookingServiceImpl.deleteBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).deleteById(id);
    }

    @Test
    public void listBookings() {
        // given
        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingViewEntity> bookingViewEntities = List.of(bookingViewEntity);

        when(bookingRepositoryMock.findAllViews()).thenReturn(bookingViewEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listBookings();

        // then
        verify(bookingMapperImpl, times(1)).bookingViewEntitiesToBookingViewDtoList(bookingViewEntities);
        verify(bookingRepositoryMock, times(1)).findAllViews();
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void listUserBookings() {
        // given
        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingViewEntity> bookingViewEntities = List.of(bookingViewEntity);

        final int userId = 1;
        when(bookingRepositoryMock.findAllUserBookingViews(userId)).thenReturn(bookingViewEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listUserBookings(userId);

        // then
        verify(bookingMapperImpl, times(1)).bookingViewEntitiesToBookingViewDtoList(bookingViewEntities);
        verify(bookingRepositoryMock, times(1)).findAllUserBookingViews(userId);
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

}
