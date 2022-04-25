package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.LessonMapperImpl;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
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
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Spy
    private LessonMapperImpl lessonMapperImpl;

    @Mock
    private LessonRepository lessonRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    private final int id = 1;
    private final int userTrainerId = 1;
    private final int newUserTrainerId = 2;
    private final int danceStyleId = 13;
    private final int duration = 14;
    private final int roomId = 14;
    private final String firstName = "Alex";
    private final String lastName = "Smirnov";
    private final String danceStyleName = "Popping";
    private final LocalDateTime startDatetime = LocalDateTime.now();
    private final boolean isDeleted = false;
    private final String timeZone = "Europe/Moscow";
    private final ZonedDateTime localDateTimeZoned = startDatetime.atZone(ZoneId.of(timeZone));
    private final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));

    final UserEntity userTrainer = UserEntity.builder()
            .id(userTrainerId)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    final UserEntity newUserTrainer = UserEntity.builder().id(newUserTrainerId).build();
    final DanceStyleEntity danceStyle = DanceStyleEntity.builder().id(danceStyleId).name(danceStyleName).build();
    final RoomEntity room = RoomEntity.builder().id(roomId).build();
    private final LessonDto lessonDto = LessonDto.builder()
            .userTrainerId(userTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
            .duration(String.valueOf(duration))
            .roomId(roomId)
            .isDeleted(isDeleted)
            .timeZone(timeZone)
            .build();
    private final LessonEntity lessonEntity = LessonEntity.builder()
            .id(id)
            .userTrainer(userTrainer)
            .danceStyle(danceStyle)
            .startDatetime(utcZoned.toLocalDateTime())
            .duration(duration)
            .room(room)
            .isDeleted(isDeleted)
            .build();
    private final LessonViewDto lessonViewDto = LessonViewDto.builder()
            .id(id)
            .trainerFirstName(firstName)
            .trainerLastName(lastName)
            .danceStyleName(danceStyleName)
            .startDatetime(utcZoned.toLocalDateTime())
            .build();
    private final LessonDto newLessonDto = LessonDto.builder()
            .id(id)
            .userTrainerId(newUserTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
            .duration(String.valueOf(duration))
            .roomId(roomId)
            .isDeleted(isDeleted)
            .timeZone(timeZone)
            .build();
    private final LessonEntity newLessonEntity = LessonEntity.builder()
            .id(id)
            .userTrainer(newUserTrainer)
            .danceStyle(danceStyle)
            .startDatetime(utcZoned.toLocalDateTime())
            .duration(duration)
            .room(room)
            .isDeleted(isDeleted)
            .build();

    @Test
    public void createLesson() {
        // given
        when(lessonMapperImpl.lessonDtoToLessonEntity(lessonDto)).thenReturn(lessonEntity);
        when(lessonRepositoryMock.save(lessonEntity)).thenReturn(lessonEntity);
        when(userRepositoryMock.findById(lessonDto.getUserTrainerId())).thenReturn(Optional.of(UserEntity.builder().build()));

        // when
        final int lessonId = lessonServiceImpl.createLesson(lessonDto);

        // then
        verify(lessonRepositoryMock, times(1)).save(lessonEntity);
        assertEquals(id, lessonId);
    }

    @Test
    public void createLessonWhenUserDoesNotExist() {
        // given
        when(userRepositoryMock.findById(lessonDto.getUserTrainerId())).thenReturn(Optional.empty());

        // when
        assertThrows(RuntimeException.class, () -> lessonServiceImpl.createLesson(lessonDto));

        // then
        verify(lessonMapperImpl, never()).lessonDtoToLessonEntity(lessonDto);
    }

    @Test
    public void getLessonById() {
        // given
        final LessonDto lessonDtoWithUTCStartDatetime = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcZoned.toLocalDateTime()))
                .duration(String.valueOf(duration))
                .roomId(roomId)
                .isDeleted(isDeleted)
                .build();

        when(lessonMapperImpl.lessonEntityToLessonDto(lessonEntity)).thenReturn(lessonDtoWithUTCStartDatetime);
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntity));

        // when
        final LessonDto lessonDtoActual = lessonServiceImpl.getLessonById(id);

        // then
        verify(lessonRepositoryMock, times(1)).findById(id);
        assertEquals(lessonDtoWithUTCStartDatetime, lessonDtoActual);
    }

    @Test
    public void getLessonByIdWhenOptionalNull() {
        // given
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.getLessonById(id));

        // then
        verify(lessonMapperImpl, never()).lessonEntityToLessonDto(lessonEntity);
        verify(lessonRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void getLessonViewById() {
        // given
        when(lessonMapperImpl.lessonEntityToLessonViewDto(lessonEntity)).thenReturn(lessonViewDto);
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntity));

        // when
        final LessonViewDto lessonViewDtoActual = lessonServiceImpl.getLessonViewById(id);

        // then
        verify(lessonRepositoryMock, times(1)).findById(id);
        assertEquals(lessonViewDto, lessonViewDtoActual);
    }

    @Test
    public void getLessonViewByIdWhenOptionalNull() {
        // given
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.getLessonViewById(id));

        // then
        verify(lessonMapperImpl, never()).lessonEntityToLessonViewDto(lessonEntity);
        verify(lessonRepositoryMock, times(1)).findById(id);
    }

    @Test
    public void updateLessonById() {
        // given
        when(lessonMapperImpl.lessonDtoToLessonEntity(newLessonDto)).thenReturn(newLessonEntity);
        when(lessonRepositoryMock.save(newLessonEntity)).thenReturn(newLessonEntity);

        // when
        lessonServiceImpl.updateLessonById(newLessonDto, id);

        // then
        verify(lessonRepositoryMock, times(1)).save(newLessonEntity);
    }

    @Test
    public void deleteLessonById() {
        // given
        doNothing().when(lessonRepositoryMock).markAsDeletedById(id);

        // when
        lessonServiceImpl.deleteLessonById(id);

        // then
        verify(lessonRepositoryMock, times(1)).markAsDeletedById(id);
    }

    @Test
    public void listLessons() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final String trainerName = "";
        final String danceStyleName = "";
        final String date = "";
        final List<LessonViewDto> lessonViewDtoListExpected = List.of(lessonViewDto);
        final Page<LessonEntity> lessonEntities = new PageImpl<>(List.of(lessonEntity));
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        when(lessonRepositoryMock.findAll((Specification<LessonEntity>) any(), eq(pageable))).thenReturn(lessonEntities);

        // when
        final List<LessonViewDto> lessonViewDtoListActual = lessonServiceImpl.listLessons(trainerName, danceStyleName, date, pageable);

        // then
        verify(lessonRepositoryMock, times(1)).findAll((Specification<LessonEntity>) any(), eq(pageable));
        assertEquals(lessonViewDtoListExpected, lessonViewDtoListActual);
    }

    @Test
    public void listUserLessons() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<LessonViewDto> lessonViewDtoListExpected = List.of(lessonViewDto);
        final List<LessonEntity> lessonEntities = List.of(lessonEntity);
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final int userId = 1;

        when(lessonMapperImpl.lessonEntitiesToLessonViewDtoList(lessonEntities)).thenReturn(lessonViewDtoListExpected);
        when(lessonRepositoryMock.findAllByUserTrainerId(userId, pageable)).thenReturn(lessonEntities);

        // when
        final List<LessonViewDto> lessonViewDtoListActual = lessonServiceImpl.listUserLessons(userId, pageable);

        // then
        verify(lessonRepositoryMock, times(1)).findAllByUserTrainerId(userId, pageable);
        assertEquals(lessonViewDtoListExpected, lessonViewDtoListActual);
    }

    @Test
    public void numberOfFilteredLessons() {
        // given
        final String trainerName = "";
        final String danceStyleName = "";
        final String date = "";
        final int numberOfFilteredLessons = 5;
        final int numberOfFilteredLessonsExpected = 5;

        when(lessonRepositoryMock.count((Specification<LessonEntity>) any())).thenReturn((long) numberOfFilteredLessons);

        // when
        final int numberOfFilteredLessonsActual = lessonServiceImpl.numberOfFilteredLessons(trainerName, danceStyleName, date);

        // then
        verify(lessonRepositoryMock, times(1)).count((Specification<LessonEntity>) any());
        assertEquals(numberOfFilteredLessonsExpected, numberOfFilteredLessonsActual);
    }

    @Test
    public void numberOfUserLessons() {
        final int userId = 1;

        // given
        final int numberOfUserLessons = 5;
        final int numberOfUserLessonsExpected = 5;

        when(lessonRepositoryMock.countAllByUserTrainerId(userId)).thenReturn(numberOfUserLessons);

        // when
        final int numberOfFilteredLessonsActual = lessonServiceImpl.numberOfUserLessons(userId);

        // then
        verify(lessonRepositoryMock, times(1)).countAllByUserTrainerId(userId);
        assertEquals(numberOfUserLessonsExpected, numberOfFilteredLessonsActual);
    }

}
