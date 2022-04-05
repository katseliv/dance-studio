package com.dataart.dancestudio.test;

import com.dataart.dancestudio.mapper.LessonMapperImpl;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.service.LessonServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {

    @Spy
    private LessonMapperImpl lessonMapperImpl;

    @Mock
    private LessonRepository lessonRepositoryMock;

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    private LessonDto lessonDto;
    private LessonEntity lessonEntity;
    private LessonViewDto lessonViewDto;
    private LessonViewEntity lessonViewEntity;
    private LessonDto newLessonDto;
    private LessonEntity newLessonEntity;

    private final int id = 1;
    private final int userTrainerId = 1;
    private final int danceStyleId = 13;
    private final int duration = 14;
    private final int roomId = 14;
    private final LocalDateTime startDatetime = LocalDateTime.now();
    private final boolean isDeleted = false;
    private final String timeZone = "Europe/Moscow";
    private final LocalDateTime localDateTime = LocalDateTime.parse(startDatetime.toString());
    private final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.of(timeZone));
    private final ZonedDateTime utcZoned = localDateTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));

    @Before
    public void init() {
        lessonDto = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .timeZone(timeZone)
                .build();
        lessonEntity = LessonEntity.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(utcZoned.toLocalDateTime())
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .build();

        final String firstName = "Alex";
        final String lastName = "Smirnov";
        final String danceStyle = "Popping";

        lessonViewDto = LessonViewDto.builder()
                .trainerFirstName(firstName)
                .trainerLastName(lastName)
                .danceStyleName(danceStyle)
                .startDatetime(startDatetime)
                .build();
        lessonViewEntity = LessonViewEntity.builder()
                .trainerFirstName(firstName)
                .trainerLastName(lastName)
                .danceStyleName(danceStyle)
                .startDatetime(startDatetime)
                .build();

        final int newUserTrainerId = 2;

        newLessonDto = LessonDto.builder()
                .userTrainerId(newUserTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(startDatetime))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .timeZone(timeZone)
                .build();
        newLessonEntity = LessonEntity.builder()
                .userTrainerId(newUserTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(utcZoned.toLocalDateTime())
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .build();
    }

    @Test
    public void createLesson() {
        //given
        when(lessonRepositoryMock.save(lessonEntity)).thenReturn(id);

        // when
        final int lessonId = lessonServiceImpl.createLesson(lessonDto);

        // then
        verify(lessonMapperImpl, times(1)).lessonDtoToLessonEntity(lessonDto);
        assertEquals(id, lessonId);
    }

    @Test
    public void getLessonById() {
        //given
        lessonDto = LessonDto.builder()
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
        assertEquals(lessonDto, lessonDtoActual);
    }

    @Test
    public void getLessonViewById() {
        //given
        when(lessonRepositoryMock.findViewById(id)).thenReturn(Optional.of(lessonViewEntity));

        // when
        final LessonViewDto lessonViewDtoActual = lessonServiceImpl.getLessonViewById(id);

        // then
        verify(lessonMapperImpl, times(1)).lessonViewEntityToLessonViewDto(lessonViewEntity);
        verify(lessonRepositoryMock, times(1)).findViewById(id);
        assertEquals(lessonViewDto, lessonViewDtoActual);
    }

    @Test
    public void updateLessonById() {
        //given
        final LessonEntity lessonEntityFromDB = lessonEntity;
        lessonDto = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcZoned.toLocalDateTime()))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .build();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntityFromDB));
        doNothing().when(lessonRepositoryMock).update(newLessonEntity, id);

        // when
        lessonServiceImpl.updateLessonById(newLessonDto, id);

        // then
        verify(lessonRepositoryMock, times(1)).update(newLessonEntity, id);
        verify(lessonMapperImpl, times(1)).lessonDtoToLessonEntity(newLessonDto);
    }

    @Test
    public void doesNotUpdateLessonById() {
        //given
        final LessonEntity lessonEntityFromDB = lessonEntity;
        lessonDto = LessonDto.builder()
                .userTrainerId(userTrainerId)
                .danceStyleId(danceStyleId)
                .startDatetime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcZoned.toLocalDateTime()))
                .duration(duration)
                .roomId(roomId)
                .isDeleted(isDeleted)
                .timeZone(null)
                .build();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(lessonEntityFromDB));

        // when
        lessonServiceImpl.updateLessonById(lessonDto, id);

        // then
        verify(lessonRepositoryMock, never()).update(lessonEntity, id);
        verify(lessonMapperImpl, never()).lessonDtoToLessonEntity(lessonDto);
    }

    @Test
    public void deleteLessonById() {
        //given
        doNothing().when(lessonRepositoryMock).deleteById(id);

        // when
        lessonServiceImpl.deleteLessonById(id);

        // then
        verify(lessonRepositoryMock, times(1)).deleteById(id);
    }

    @Test
    public void listLessons() {
        //given
        final List<LessonViewDto> lessonViewDtoListExpected = new ArrayList<>();
        lessonViewDtoListExpected.add(lessonViewDto);

        final List<LessonViewEntity> lessonViewEntities = new ArrayList<>();
        lessonViewEntities.add(lessonViewEntity);

        when(lessonRepositoryMock.findAllViews()).thenReturn(lessonViewEntities);

        // when
        final List<LessonViewDto> lessonViewDtoListActual = lessonServiceImpl.listLessons();

        // then
        verify(lessonMapperImpl, times(1))
                .lessonViewEntitiesToLessonViewDtoList(lessonViewEntities);
        verify(lessonRepositoryMock, times(1)).findAllViews();
        assertEquals(lessonViewDtoListExpected, lessonViewDtoListActual);
    }

}
