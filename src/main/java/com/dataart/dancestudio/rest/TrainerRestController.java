package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.service.LessonPaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerRestController {

    private final LessonPaginationService lessonPaginationService;

    @Autowired
    public TrainerRestController(final LessonPaginationService lessonPaginationService) {
        this.lessonPaginationService = lessonPaginationService;
    }

    @GetMapping(path = "/{id}/lessons")
    public List<LessonViewDto> getTrainerLessons(@RequestParam(required = false) final Map<String, String> allParams,
                                                 @PathVariable final int id) {
        final UserLessonViewListPage userLessonViewListPage = lessonPaginationService
                .getUserLessonViewListPage(id, allParams.get("page"), allParams.get("size"));
        return userLessonViewListPage.getUserLessonViewDtoList();
    }

}
