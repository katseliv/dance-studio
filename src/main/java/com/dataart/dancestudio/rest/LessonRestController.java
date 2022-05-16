package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.service.LessonPaginationService;
import com.dataart.dancestudio.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonRestController {

    private final LessonService lessonService;
    private final LessonPaginationService lessonPaginationService;

    @Autowired
    public LessonRestController(final LessonService lessonService, final LessonPaginationService lessonPaginationService) {
        this.lessonService = lessonService;
        this.lessonPaginationService = lessonPaginationService;
    }

    @PostMapping
    public int createLesson(@RequestBody @Valid final LessonDto lessonDto) {
        return lessonService.createLesson(lessonDto);
    }

    @GetMapping("/{id}")
    public LessonViewDto getLesson(@PathVariable final int id) {
        return lessonService.getLessonViewById(id);
    }

    @PutMapping("/{id}")
    public void updateLesson(@RequestBody @Valid final LessonDto lessonDto, @PathVariable final int id) {
        lessonService.updateLessonById(lessonDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable final int id) {
        lessonService.deleteLessonById(id);
    }

    @GetMapping
    public List<LessonViewDto> getLessons(@RequestParam(required = false) final Map<String, String> allParams) {
        final FilteredLessonViewListPage filteredLessonViewListPage = lessonPaginationService
                .getFilteredLessonViewListPage(allParams.get("page"), allParams.get("size"),
                        allParams.get("trainerName"), allParams.get("styleName"), allParams.get("date"));
        return filteredLessonViewListPage.getLessonViewDtoList();
    }

}
