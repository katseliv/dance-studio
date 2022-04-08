package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final LessonService lessonService;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public TrainerController(final LessonService lessonService, final AuthenticationManager authenticationManager) {
        this.lessonService = lessonService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/{id}/lessons")
    public String getTrainerLessons(final Model model, @PathVariable final int id) {
        model.addAttribute("lessons", lessonService.listUserLessons(id));
        return "lists/lesson_list";
    }

}
