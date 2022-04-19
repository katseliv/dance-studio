package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.BookingMapperImpl;
import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.entity.BookingEntity;
import com.dataart.dancestudio.model.entity.NewLessonEntity;
import com.dataart.dancestudio.model.entity.NewUserEntity;
import com.dataart.dancestudio.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final BookingEntity bookingEntity = new BookingEntity();
    private final BookingViewDto bookingViewDto = BookingViewDto.builder()
            .firstName(firstName)
            .lastName(lastName)
            .danceStyle(danceStyle)
            .startDatetime(startDatetime)
            .build();

    @BeforeEach
    public void initBookingEntity() {
        final NewUserEntity newUserEntity = new NewUserEntity();
        newUserEntity.setId(userId);
        final NewLessonEntity newLessonEntity = new NewLessonEntity();
        newLessonEntity.setId(lessonId);
        bookingEntity.setId(id);
        bookingEntity.setUser(new NewUserEntity());
        bookingEntity.setLesson(newLessonEntity);
        bookingEntity.setIsDeleted(isDeleted);
    }

    @Test
    public void createBooking() {
        // given
        when(bookingMapperImpl.bookingDtoToBookingEntity(bookingDto)).thenReturn(bookingEntity);
        when(bookingRepositoryMock.save(bookingEntity)).thenReturn(bookingEntity);

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
        when(bookingRepositoryMock.findBookingEntityByIdAndIsDeletedFalse(id)).thenReturn(bookingEntity);

        // when
        final BookingDto bookingDtoActual = bookingServiceImpl.getBookingById(id);

        // then
        verify(bookingRepositoryMock, times(1)).findBookingEntityByIdAndIsDeletedFalse(id);
        assertEquals(bookingDto, bookingDtoActual);
    }

    @Test
    public void getBookingViewById() {
        // given
        when(bookingMapperImpl.bookingEntityToBookingViewDto(bookingEntity)).thenReturn(bookingViewDto);
        when(bookingRepositoryMock.findBookingEntityByIdAndIsDeletedFalse(id)).thenReturn(bookingEntity);

        // when
        final BookingViewDto bookingViewDtoActual = bookingServiceImpl.getBookingViewById(id);

        // then
        verify(bookingRepositoryMock, times(1)).findBookingEntityByIdAndIsDeletedFalse(id);
        assertEquals(bookingViewDto, bookingViewDtoActual);
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
        when(bookingRepositoryMock.findAllByIsDeletedFalse()).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listBookings();

        // then
        verify(bookingRepositoryMock, times(1)).findAllByIsDeletedFalse();
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

    @Test
    public void listUserBookings() {
        // given
        final List<BookingViewDto> bookingViewDtoListExpected = List.of(bookingViewDto);
        final List<BookingEntity> bookingEntities = List.of(bookingEntity);

        final int userId = 1;
        when(bookingMapperImpl.bookingEntitiesToBookingViewDtoList(bookingEntities)).thenReturn(bookingViewDtoListExpected);
        when(bookingRepositoryMock.findBookingEntitiesByUserIdAndIsDeletedFalse(userId)).thenReturn(bookingEntities);

        // when
        final List<BookingViewDto> bookingViewDtoListActual = bookingServiceImpl.listUserBookings(userId);

        // then
        verify(bookingRepositoryMock, times(1)).findBookingEntitiesByUserIdAndIsDeletedFalse(userId);
        assertEquals(bookingViewDtoListExpected, bookingViewDtoListActual);
    }

}
