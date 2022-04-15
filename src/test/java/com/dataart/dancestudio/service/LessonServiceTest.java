package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.LessonMapperImpl;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private final LocalDateTime localDateTime = LocalDateTime.parse(startDatetime.toString());
    private final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(timeZone));
    private final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));

    private final LessonDto lessonDto = LessonDto.builder()
            .userTrainerId(userTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
            .duration(duration)
            .roomId(roomId)
            .isDeleted(isDeleted)
            .timeZone(timeZone)
            .build();
    private final LessonEntity lessonEntity = LessonEntity.builder()
            .userTrainerId(userTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(utcZoned.toLocalDateTime())
            .duration(duration)
            .roomId(roomId)
            .isDeleted(isDeleted)
            .build();
    private final LessonViewDto lessonViewDto = LessonViewDto.builder()
            .trainerFirstName(firstName)
            .trainerLastName(lastName)
            .danceStyleName(danceStyleName)
            .startDatetime(startDatetime)
            .build();
    private final LessonViewEntity lessonViewEntity = LessonViewEntity.builder()
            .trainerFirstName(firstName)
            .trainerLastName(lastName)
            .danceStyleName(danceStyleName)
            .startDatetime(startDatetime)
            .build();
    private final LessonDto newLessonDto = LessonDto.builder()
            .userTrainerId(newUserTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
            .duration(duration)
            .roomId(roomId)
            .isDeleted(isDeleted)
            .timeZone(timeZone)
            .build();
    private final LessonEntity newLessonEntity = LessonEntity.builder()
            .userTrainerId(newUserTrainerId)
            .danceStyleId(danceStyleId)
            .startDatetime(utcZoned.toLocalDateTime())
            .duration(duration)
            .roomId(roomId)
            .isDeleted(isDeleted)
            .build();

    @Test
    public void createLesson() {
        // given
        when(lessonRepositoryMock.save(lessonEntity)).thenReturn(id);
        when(userRepositoryMock.findById(lessonDto.getUserTrainerId())).thenReturn(Optional.of(UserEntity.builder().build()));

        // when
        final int lessonId = lessonServiceImpl.createLesson(lessonDto);

        // then
        verify(lessonMapperImpl, times(1)).lessonDtoToLessonEntity(lessonDto);
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
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .build();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntity));

        // when
        final LessonDto lessonDtoActual = lessonServiceImpl.getLessonById(id);

        // then
        verify(lessonMapperImpl, times(1)).lessonEntityToLessonDto(lessonEntity);
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
        when(lessonRepositoryMock.findViewById(id)).thenReturn(Optional.of(lessonViewEntity));

        // when
        final LessonViewDto lessonViewDtoActual = lessonServiceImpl.getLessonViewById(id);

        // then
        verify(lessonMapperImpl, times(1)).lessonViewEntityToLessonViewDto(lessonViewEntity);
        verify(lessonRepositoryMock, times(1)).findViewById(id);
        assertEquals(lessonViewDto, lessonViewDtoActual);
    }

    @Test
    public void getLessonViewByIdWhenOptionalNull() {
        // given
        when(lessonRepositoryMock.findViewById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.getLessonViewById(id));

        // then
        verify(lessonMapperImpl, never()).lessonViewEntityToLessonViewDto(lessonViewEntity);
        verify(lessonRepositoryMock, times(1)).findViewById(id);
    }

    @Test
    public void updateLessonById() {
        // given
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntity));
        doNothing().when(lessonRepositoryMock).update(newLessonEntity, id);

        // when
        lessonServiceImpl.updateLessonById(newLessonDto, id);

        // then
        verify(lessonRepositoryMock, times(1)).update(newLessonEntity, id);
        verify(lessonMapperImpl, times(1)).lessonDtoToLessonEntity(newLessonDto);
    }

    @Test
    public void updateLessonByIdWhenOptionalNull() {
        // given
        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.updateLessonById(newLessonDto, id));

        // then
        verify(lessonRepositoryMock, never()).update(newLessonEntity, id);
        verify(lessonMapperImpl, never()).lessonDtoToLessonEntity(newLessonDto);
    }

    @Test
    public void doesNotUpdateLessonById() {
        // given
        final LessonDto lessonDto = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcZoned.toLocalDateTime()))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .timeZone(null)
                .build();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntity));

        // when
        lessonServiceImpl.updateLessonById(lessonDto, id);

        // then
        verify(lessonRepositoryMock, never()).update(lessonEntity, id);
        verify(lessonMapperImpl, never()).lessonDtoToLessonEntity(lessonDto);
    }

    @Test
    public void doesNotUpdateLessonByIdWhenOptionalNull() {
        // given
        final LessonDto lessonDtoWithoutTimeZone = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcZoned.toLocalDateTime()))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .timeZone(null)
                .build();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.updateLessonById(lessonDtoWithoutTimeZone, id));

        // then
        verify(lessonRepositoryMock, never()).update(lessonEntity, id);
        verify(lessonMapperImpl, never()).lessonDtoToLessonEntity(lessonDtoWithoutTimeZone);
    }

    @Test
    public void deleteLessonById() {
        // given
        doNothing().when(lessonRepositoryMock).markAsDeleted(id);

        // when
        lessonServiceImpl.deleteLessonById(id);

        // then
        verify(lessonRepositoryMock, times(1)).markAsDeleted(id);
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
        final List<LessonViewEntity> lessonViewEntities = List.of(lessonViewEntity);


        when(lessonRepositoryMock.findAllViews(trainerName, danceStyleName, date, pageNumber, pageNumber * pageSize)).thenReturn(lessonViewEntities);

        // when
        final List<LessonViewDto> lessonViewDtoListActual = lessonServiceImpl.listLessons(trainerName, danceStyleName, date, pageNumber, pageNumber * pageSize);

        // then
        verify(lessonMapperImpl, times(1)).lessonViewEntitiesToLessonViewDtoList(lessonViewEntities);
        verify(lessonRepositoryMock, times(1)).findAllViews(trainerName, danceStyleName, date, pageNumber, pageNumber * pageSize);
        assertEquals(lessonViewDtoListExpected, lessonViewDtoListActual);
    }

    @Test
    public void listUserLessons() {
        // given
        final int pageNumber = 1;
        final int pageSize = 5;

        final List<LessonViewDto> lessonViewDtoListExpected = List.of(lessonViewDto);
        final List<LessonViewEntity> lessonViewEntities = List.of(lessonViewEntity);

        final int userId = 1;
        when(lessonRepositoryMock.findAllUserLessonViews(userId, pageNumber, pageNumber * pageSize)).thenReturn(lessonViewEntities);

        // when
        final List<LessonViewDto> lessonViewDtoListActual = lessonServiceImpl.listUserLessons(userId, pageNumber, pageNumber * pageSize);

        // then
        verify(lessonMapperImpl, times(1)).lessonViewEntitiesToLessonViewDtoList(lessonViewEntities);
        verify(lessonRepositoryMock, times(1)).findAllUserLessonViews(userId, pageNumber, pageNumber * pageSize);
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

        when(lessonRepositoryMock.numberOfFilteredLessons(trainerName, danceStyleName, date)).thenReturn(Optional.of(numberOfFilteredLessons));

        // when
        final int numberOfFilteredLessonsActual = lessonServiceImpl.numberOfFilteredLessons(trainerName, danceStyleName, date);

        // then
        verify(lessonRepositoryMock, times(1)).numberOfFilteredLessons(trainerName, danceStyleName, date);
        assertEquals(numberOfFilteredLessonsExpected, numberOfFilteredLessonsActual);
    }

    @Test
    public void numberOfFilteredLessonsWhenOptionalNull() {
        // given
        final String trainerName = "";
        final String danceStyleName = "";
        final String date = "";

        when(lessonRepositoryMock.numberOfFilteredLessons(trainerName, danceStyleName, date)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.numberOfFilteredLessons(trainerName, danceStyleName, date));

        // then
        verify(lessonRepositoryMock, times(1)).numberOfFilteredLessons(trainerName, danceStyleName, date);
    }

    @Test
    public void numberOfUserLessons() {
        final int userId = 1;

        // given
        final int numberOfUserLessons = 5;
        final int numberOfUserLessonsExpected = 5;

        when(lessonRepositoryMock.numberOfUserLessons(userId)).thenReturn(Optional.of(numberOfUserLessons));

        // when
        final int numberOfFilteredLessonsActual = lessonServiceImpl.numberOfUserLessons(userId);

        // then
        verify(lessonRepositoryMock, times(1)).numberOfUserLessons(userId);
        assertEquals(numberOfUserLessonsExpected, numberOfFilteredLessonsActual);
    }

    @Test
    public void numberOfUserLessonsWhenOptionalNull() {
        // given
        final int userId = 1;

        when(lessonRepositoryMock.numberOfUserLessons(userId)).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> lessonServiceImpl.numberOfUserLessons(userId));

        // then
        verify(lessonRepositoryMock, times(1)).numberOfUserLessons(userId);
    }

}
