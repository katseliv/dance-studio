package com.dataart.dancestudio.test;

import com.dataart.dancestudio.mapper.BookingMapperImpl;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.view.BookingViewEntity;
import com.dataart.dancestudio.repository.impl.BookingRepository;
import com.dataart.dancestudio.service.impl.BookingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @Spy
    private BookingMapperImpl bookingMapperImpl;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private int id;
    private BookingDto bookingDto;
    private BookingEntity bookingEntity;
    private BookingViewDto bookingViewDto;
    private BookingViewEntity bookingViewEntity;
    private BookingDto newBookingDto;
    private BookingEntity newBookingEntity;

    @Before
    public void init() {
        id = 1;
        final int userId = 13;
        final int lessonId = 14;
        final boolean isDeleted = false;

        bookingDto = BookingDto.builder()
                .userId(userId)
                .lessonId(lessonId)
                .isDeleted(isDeleted)
                .build();
        bookingEntity = BookingEntity.builder()
                .userId(userId)
                .lessonId(lessonId)
                .isDeleted(isDeleted)
                .build();

        final String firstName = "Alex";
        final String lastName = "Smirnov";
        final String danceStyle = "Popping";
        final LocalDateTime startDatetime = LocalDateTime.now();

        bookingViewDto = BookingViewDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .danceStyle(danceStyle)
                .startDatetime(startDatetime)
                .build();
        bookingViewEntity = BookingViewEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .danceStyle(danceStyle)
                .startDatetime(startDatetime)
                .build();

        final int newUserId = 2300;
        final int newLessonId = 1400;

        newBookingDto = BookingDto.builder()
                .userId(newUserId)
                .lessonId(newLessonId)
                .isDeleted(isDeleted)
                .build();
        newBookingEntity = BookingEntity.builder()
                .userId(newUserId)
                .lessonId(newLessonId)
                .isDeleted(isDeleted)
                .build();
    }

    @Test
    public void createBooking() {
        //given
        when(bookingRepositoryMock.save(bookingEntity)).thenReturn(id);

        // when
        final int bookingId = bookingServiceImpl.createBooking(bookingDto);

        // then
        verify(bookingMapperImpl, times(1)).bookingDtoToBookingEntity(bookingDto);
        assertEquals(id, bookingId);
    }

    @Test
    public void getBookingById() {
        //given
        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntity));

        // when
        final BookingDto bookingDtoActual = bookingServiceImpl.getBookingById(id);

        // then
        verify(bookingMapperImpl, times(1)).bookingEntityToBookingDto(bookingEntity);
        verify(bookingRepositoryMock, times(1)).findById(id);
        assertEquals(bookingDto, bookingDtoActual);
    }

    @Test
    public void getBookingViewById() {
        //given
        when(bookingRepositoryMock.findViewById(id)).thenReturn(Optional.of(bookingViewEntity));

        // when
        final BookingViewDto bookingViewDtoActual = bookingServiceImpl.getBookingViewById(id);

        // then
        verify(bookingMapperImpl, times(1)).bookingViewEntityToBookingViewDto(bookingViewEntity);
        verify(bookingRepositoryMock, times(1)).findViewById(id);
        assertEquals(bookingViewDto, bookingViewDtoActual);
    }

    @Test
    public void updateBookingById() {
        //given
        final BookingEntity bookingEntityFromDB = bookingEntity;

        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntityFromDB));
        doNothing().when(bookingRepositoryMock).update(newBookingEntity, id);

        // when
        bookingServiceImpl.updateBookingById(newBookingDto, id);

        // then
        verify(bookingRepositoryMock, times(1)).update(newBookingEntity, id);
        verify(bookingMapperImpl, times(1)).bookingDtoToBookingEntity(newBookingDto);
    }

    @Test
    public void doesNotUpdateBookingById() {
        //given
        final BookingEntity bookingEntityFromDB = bookingEntity;

        when(bookingRepositoryMock.findById(id)).thenReturn(Optional.of(bookingEntityFromDB));

        // when
        bookingServiceImpl.updateBookingById(bookingDto, id);

        // then
        verify(bookingRepositoryMock, never()).update(newBookingEntity, id);
        verify(bookingMapperImpl, never()).bookingDtoToBookingEntity(bookingDto);
    }

    @Test
    public void deleteBookingById() {
        //given
        doNothing().when(bookingRepositoryMock).deleteById(id);

        // when
        bookingServiceImpl.deleteBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).deleteById(id);
    }

    @Test
    public void listBookings() {
        //given
        final List<BookingViewDto> bookingViewDtoListExpected = new ArrayList<>();
        bookingViewDtoListExpected.add(bookingViewDto);

        final List<BookingViewEntity> bookingViewEntities = new ArrayList<>();
        bookingViewEntities.add(bookingViewEntity);

        when(bookingRepositoryMock.findAllViews()).thenReturn(bookingViewEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listBookings();

        // then
        verify(bookingMapperImpl, times(1))
                .bookingViewEntitiesToBookingViewDtoList(bookingViewEntities);
        verify(bookingRepositoryMock, times(1)).findAllViews();
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

}
