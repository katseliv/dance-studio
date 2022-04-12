package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonService {

    int createLesson(LessonDto lessonDto);

    LessonDto getLessonById(int id);

    LessonViewDto getLessonViewById(int id);

    void updateLessonById(LessonDto lessonDto, int id);

    void deleteLessonById(int id);

    Page<LessonViewDto> listLessons(String trainerName, String danceStyleName, String date, Pageable pageable);

    Page<LessonViewDto> listUserLessons(String trainerName, String danceStyleName, String date, Pageable pageable, int userId);

}
