package com.dataart.dancestudio.service.logic.impl;

import com.dataart.dancestudio.db.repository.impl.LessonRepository;
import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.mapper.LessonMapper;
import com.dataart.dancestudio.service.model.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository repository;
    private final LessonMapper mapper;

    @Autowired
    public LessonServiceImpl(LessonRepository repository, LessonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void createLesson(LessonDto lessonDto) {
        repository.save(mapper.toEntity(lessonDto));
    }

    @Override
    public LessonDto getLessonById(int id) {
        return mapper.fromEntity(repository.findById(id).orElse(null));
    }

    @Override
    public void updateLessonById(LessonDto lessonDto, int id) {
        repository.update(mapper.toEntity(lessonDto), id);
    }

    @Override
    public void deleteLessonById(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<LessonDto> getAllLessons() {
        return mapper.fromEntities(repository.findAll());
    }

}
