package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LessonEntity;
import com.dataart.dancestudio.model.entity.view.LessonViewEntity;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends Repository<LessonEntity> {

    Optional<LessonViewEntity> findViewById(final int id);

    List<LessonViewEntity> findAllViews();

    List<LessonViewEntity> findAllUserLessonViews(final int userId);

}
