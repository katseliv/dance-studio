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

    void updateLessonById(LessonDto lessonDto, int id);

    void deleteLessonById(int id);

    List<LessonViewDto> listLessons(String trainerName, String danceStyleName, String date, Pageable pageable);

    int numberOfFilteredLessons(String trainerName, String danceStyleName, String date);

    List<LessonViewDto> listUserLessons(int userId, Pageable pageable);

    int numberOfUserLessons(int userId);

    ViewListPage<LessonViewDto> getUserViewListPage(int id, String page, String size);

    FilteredViewListPage<LessonViewDto> getFilteredLessonViewListPage(String page, String size, String trainerName,
                                                                      String styleName, String date);

}
