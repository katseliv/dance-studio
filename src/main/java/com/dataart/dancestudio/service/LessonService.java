package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.FilteredViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonService {

    int createLesson(LessonDto lessonDto);

    LessonDto getLessonById(int id);

    LessonViewDto getLessonViewById(int id);

    void updateLessonById(int id, LessonDto lessonDto);

    void deleteLessonById(int id);

    FilteredViewListPage<LessonViewDto> getFilteredLessonViewListPage(String page, String size, String trainerName,
                                                                      String styleName, String date);

    ViewListPage<LessonViewDto> getUserViewListPage(int id, String page, String size);

    List<LessonViewDto> listLessons(String trainerName, String danceStyleName, String date, Pageable pageable);

    int numberOfFilteredLessons(String trainerName, String danceStyleName, String date);

    List<LessonViewDto> listUserLessons(int userId, Pageable pageable);

    int numberOfUserLessons(int userId);

}
