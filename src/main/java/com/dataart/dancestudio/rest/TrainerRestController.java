package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerRestController {

    private final PaginationService<LessonViewDto> lessonPaginationService;

    @GetMapping(path = "/{id}/lessons")
    public ViewListPage<LessonViewDto> getTrainerLessons(@RequestParam(required = false) final Map<String, String> allParams,
                                                         @PathVariable final int id) {
        return lessonPaginationService.getUserViewListPage(id, allParams.get("page"), allParams.get("size"));
    }

}
