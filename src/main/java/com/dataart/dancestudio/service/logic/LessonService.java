package com.dataart.dancestudio.service.logic;

import com.dataart.dancestudio.service.model.LessonDto;

import java.util.List;

public interface LessonService {

    void createLesson(LessonDto lessonDto);

    LessonDto getLessonById(int id);

    void updateLessonById(LessonDto lessonDto, int id);

    void deleteLessonById(int id);

    List<LessonDto> getAllLessons();

}
