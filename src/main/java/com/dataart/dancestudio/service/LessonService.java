package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;

import java.util.List;

public interface LessonService {

    int createLesson(LessonDto lessonDto);

    LessonDto getLessonById(int id);

    LessonViewDto getLessonViewById(int id);

    void updateLessonById(LessonDto lessonDto, int id);

    void deleteLessonById(int id);

    List<LessonViewDto> listLessons(String trainerName, String danceStyleName, String date, int limit, int offset);

    List<LessonViewDto> listUserLessons(int userId, int limit, int offset);

    int numberOfFilteredLessons(String trainerName, String danceStyleName, String date);

    int numberOfUserLessons(int userId);

}
