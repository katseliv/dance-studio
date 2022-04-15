package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.LessonMapper;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.repository.LessonRepository;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonServiceImpl(final UserRepository userRepository, final LessonRepository lessonRepository, final LessonMapper lessonMapper) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public int createLesson(final LessonDto lessonDto) {
        if (userRepository.findById(lessonDto.getUserTrainerId()).isPresent()) {
            return lessonRepository.save(lessonMapper.lessonDtoToLessonEntity(lessonDto));
        } else {
            log.info("Lesson wasn't created");
            throw new RuntimeException("Lesson wasn't created");
        }
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
        lessonRepository.markAsDeleted(id);
    }

    @Override
    public List<LessonViewDto> listLessons(final String trainerName, final String danceStyleName, final String date, final int limit, final int offset) {
        return lessonMapper.lessonViewEntitiesToLessonViewDtoList(lessonRepository.findAllViews(trainerName, danceStyleName, date, limit, offset));
    }

    @Override
    public List<LessonViewDto> listUserLessons(final int userId, final int limit, final int offset) {
        return lessonMapper.lessonViewEntitiesToLessonViewDtoList(lessonRepository.findAllUserLessonViews(userId, limit, offset));
    }

    @Override
    public int numberOfFilteredLessons(final String trainerName, final String danceStyleName, final String date) {
        return lessonRepository.numberOfFilteredLessons(trainerName, danceStyleName, date).orElseThrow();
    }

    @Override
    public int numberOfUserLessons(final int userId) {
        return lessonRepository.numberOfUserLessons(userId).orElseThrow();
    }

}
