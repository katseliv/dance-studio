package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.PaginationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final PaginationService<LessonViewDto> lessonPaginationService;
    protected AuthenticationManager authenticationManager;

    @GetMapping(path = "/{id}/lessons")
    public String getTrainerLessons(@RequestParam(required = false) final Map<String, String> allParams,
                                    @PathVariable final int id, final Model model) {
        final ViewListPage<LessonViewDto> lessonViewListPage = lessonPaginationService
                .getUserViewListPage(id, allParams.get("page"), allParams.get("size"));

        model.addAttribute("lessonPage", lessonViewListPage);
        model.addAttribute("booking", BookingDto.builder().build());
        return "lists/trainer_lesson_list";
    }

}
