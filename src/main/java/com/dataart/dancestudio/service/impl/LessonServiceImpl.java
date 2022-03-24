package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.repository.impl.LessonRepository;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
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
        return lessonRepository.save(lessonMapper.toEntity(lessonDto));
    }

    @Override
    public LessonDto getLessonById(final int id) {
        return lessonMapper.fromEntity(lessonRepository.findById(id).orElseThrow());
    }

    @Override
    public LessonViewDto getLessonViewById(final int id) {
        return lessonMapper.fromEntity(lessonRepository.findViewById(id).orElseThrow());
    }

    @Override
    public void updateLessonById(final LessonDto lessonDto, final int id) {
        lessonRepository.update(lessonMapper.toEntity(lessonDto), id);
    }

    @Override
    public void deleteLessonById(final int id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public List<LessonViewDto> getAllLessons() {
        return lessonMapper.fromEntities(lessonRepository.findAllViews());
    }

}
