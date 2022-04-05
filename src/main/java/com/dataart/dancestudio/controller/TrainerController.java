package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.ContextFacade;
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

    private final UserService userService;
    private final LessonService lessonService;
    private final ContextFacade contextFacade;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public TrainerController(final UserService userService, final LessonService lessonService,
                             final ContextFacade contextFacade, final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.lessonService = lessonService;
        this.contextFacade = contextFacade;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/operations")
    public String getTrainerOperations(final Model model) {
        final String email = contextFacade.getContext().getAuthentication().getName();
        model.addAttribute("id", userService.getUserIdByEmail(email));
        return "operations/trainer_operations";
    }

    @GetMapping("/{id}/lessons")
    public String getTrainerLessons(final Model model, @PathVariable final int id) {
        model.addAttribute("lessons", lessonService.listUserLessons(id));
        return "lists/lesson_list";
    }

}
