package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<LessonViewDto> listLessons() {
        return lessonMapper.lessonViewEntitiesToLessonViewDtoList(lessonRepository.findAllViews());
    }

    @Override
    public List<LessonViewDto> listUserLessons(final int userId) {
        return lessonMapper.lessonViewEntitiesToLessonViewDtoList(lessonRepository.findAllUserLessonViews(userId));
    }

}
