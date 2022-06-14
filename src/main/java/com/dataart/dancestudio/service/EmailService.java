package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;

import java.util.List;
import java.util.Map;

public interface EmailService {

    void sendEmailAboutStartingLesson(Map<String, List<BookingViewDto>> emailAndBookings);

    void sendEmailAboutChangingLesson(LessonViewDto lessonViewDto);

    void sendEmailAboutCancelingLesson(LessonViewDto lessonViewDto);

}
