package com.dataart.dancestudio.service.logic;

import com.dataart.dancestudio.service.model.LessonDto;
import com.dataart.dancestudio.service.model.view.LessonViewDto;

import java.util.List;

public interface LessonService {

    void createLesson(LessonDto lessonDto);

    LessonDto getLessonById(int id);

    LessonViewDto getLessonViewById(int id);

    void updateLessonById(LessonDto lessonDto, int id);

    void deleteLessonById(int id);

    List<LessonViewDto> getAllLessons();

}
