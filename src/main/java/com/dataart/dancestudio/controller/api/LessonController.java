package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.model.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService service;

    @Autowired
    public LessonController(LessonService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public void createLesson(@RequestBody LessonDto lessonDto) {
        service.createLesson(lessonDto);
    }

    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable int id) {
        return service.getLessonById(id);
    }

    @PutMapping("/update/{id}")
    public void updateLesson(@RequestBody LessonDto lessonDto, @PathVariable int id) {
        service.updateLessonById(lessonDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLesson(@PathVariable int id) {
        service.deleteLessonById(id);
    }

    @GetMapping("/")
    public List<LessonDto> getLessons() {
        return service.getAllLessons();
    }

}
