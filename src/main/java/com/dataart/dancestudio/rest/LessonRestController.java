package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/lessons")
public class LessonRestController {

    private final LessonService lessonService;
    private final PaginationService<LessonViewDto> lessonPaginationService;

    @PostMapping
    public ResponseEntity<Integer> createLesson(@RequestBody @Valid final LessonDto lessonDto) {
        final int id = lessonService.createLesson(lessonDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public LessonViewDto getLesson(@PathVariable final int id) {
        return lessonService.getLessonViewById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLesson(@RequestBody @Valid final LessonDto lessonDto, @PathVariable final int id) {
        lessonService.updateLessonById(lessonDto, id);
        return new ResponseEntity<>("Lesson was updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable final int id) {
        lessonService.deleteLessonById(id);
    }

    @GetMapping
    public FilteredLessonViewListPage getLessons(@RequestParam(required = false) final Map<String, String> allParams) {
        return lessonPaginationService
                .getFilteredLessonViewListPage(allParams.get("page"), allParams.get("size"),
                        allParams.get("trainerName"), allParams.get("styleName"), allParams.get("date"));
    }

}
