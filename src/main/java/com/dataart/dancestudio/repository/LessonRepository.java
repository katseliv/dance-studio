package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends Repository<LessonEntity> {

    Optional<LessonViewEntity> findViewById(int id);

    List<LessonViewEntity> findAllViews(String trainerName, String danceStyleName, String date, int limit, long offset);

    List<LessonViewEntity> findAllUserLessonViews(int limit, long offset, int userId);

    Optional<Integer> amountOfAllLessons(String trainerName, String danceStyleName, String date);

}
