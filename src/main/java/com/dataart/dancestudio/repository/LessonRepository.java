package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends Repository<LessonEntity> {

    Optional<LessonViewEntity> findViewById(int id);

    List<LessonViewEntity> findAllViews(String trainerName, String danceStyleName, String date, int limit, long offset);

    List<LessonViewEntity> findAllUserLessonViews(int userId, int limit, long offset);

    Optional<Integer> numberOfFilteredLessons(String trainerName, String danceStyleName, String date);

    Optional<Integer> numberOfUserLessons(int userId);

}
