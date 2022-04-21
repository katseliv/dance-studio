package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.UserLessonViewListPage;
import com.dataart.dancestudio.service.LessonPaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final LessonPaginationService lessonPaginationService;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public TrainerController(final LessonPaginationService lessonPaginationService,
                             final AuthenticationManager authenticationManager) {
        this.lessonPaginationService = lessonPaginationService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping(path = "/{id}/lessons")
    public String getTrainerLessons(@RequestParam(required = false) final Map<String, String> allParams,
                                    @PathVariable final int id, final Model model) {
        final UserLessonViewListPage userLessonViewListPage = lessonPaginationService
                .getUserLessonViewListPage(id, allParams.get("page"), allParams.get("size"));

        model.addAttribute("lessonPage", userLessonViewListPage);
        model.addAttribute("booking", BookingDto.builder().build());
        return "lists/trainer_lesson_list";
    }

}
