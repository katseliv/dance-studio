package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonServiceImpl(final LessonRepository lessonRepository, final LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public int createLesson(final LessonDto lessonDto) {
        return lessonRepository.save(lessonMapper.lessonDtoToLessonEntity(lessonDto));
    }

    @Override
    public LessonDto getLessonById(final int id) {
        return lessonMapper.lessonEntityToLessonDto(lessonRepository.findById(id).orElseThrow());
    }

    @Override
    public LessonViewDto getLessonViewById(final int id) {
        return lessonMapper.lessonViewEntityToLessonViewDto(lessonRepository.findViewById(id).orElseThrow());
    }

    @Override
    public void updateLessonById(final LessonDto lessonDto, final int id) {
        final LessonDto lessonDtoFromDB = getLessonById(id);
        if (!lessonDto.equals(lessonDtoFromDB)) {
            lessonRepository.update(lessonMapper.lessonDtoToLessonEntity(lessonDto), id);
        }
    }

    @Override
    public void deleteLessonById(final int id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Page<LessonViewDto> listLessons(final String trainerName, final String danceStyleName, final String date,
                                           final Pageable pageable) {
        final List<LessonViewDto> lessonViewDtoList = lessonMapper.lessonViewEntitiesToLessonViewDtoList(
                lessonRepository.findAllViews(trainerName, danceStyleName, date, pageable.getPageSize(), pageable.getOffset()));
        return new PageImpl<>(lessonViewDtoList, pageable, lessonRepository.amountOfAllLessons(trainerName, danceStyleName,
                date).orElseThrow());
    }

    @Override
    public Page<LessonViewDto> listUserLessons(final String trainerName, final String danceStyleName, final String date,
                                               final Pageable pageable, final int userId) {
        final List<LessonViewDto> lessonViewDtoList = lessonMapper.lessonViewEntitiesToLessonViewDtoList(
                lessonRepository.findAllUserLessonViews(pageable.getPageSize(), pageable.getOffset(), userId));
        return new PageImpl<>(lessonViewDtoList, pageable, lessonRepository.amountOfAllLessons(trainerName, danceStyleName,
                date).orElseThrow());
    }

}
