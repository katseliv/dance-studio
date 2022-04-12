package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final LessonService lessonService;
    private final PaginationService paginationService;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public TrainerController(final LessonService lessonService, final PaginationService paginationService,
                             final AuthenticationManager authenticationManager) {
        this.lessonService = lessonService;
        this.paginationService = paginationService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping(path = "/{id}/lessons")
    public String getTrainerLessons(@RequestParam(name = "page", required = false) final Integer page,
                                    @RequestParam(name = "size", required = false) final Integer size,
                                    @RequestParam(name = "trainerName", required = false) final String trainerName,
                                    @RequestParam(name = "styleName", required = false) final String styleName,
                                    @RequestParam(name = "date", required = false) final String date,
                                    @PathVariable final int id, final Model model) {

        final PageRequest pageRequest = paginationService.initPageRequest(page, size);
        final Page<LessonViewDto> lessonViewDtoPage = lessonService.listUserLessons(trainerName, styleName, date, pageRequest, id);
        prepareModelForLessons(trainerName, styleName, date, pageRequest.getPageNumber(), pageRequest.getPageSize(), lessonViewDtoPage, model);

        return "lists/lesson_list";
    }

    private void prepareModelForLessons(final String trainerName, final String styleName, final String date,
                                        final Integer pageNumber, final Integer pageSize,
                                        final Page<LessonViewDto> lessonViewDtoPage, final Model model) {
        final int buttonLimit = 5;
        int totalPages = lessonViewDtoPage.getTotalPages();
        int endPageNumber = pageNumber + buttonLimit;
        if (endPageNumber > totalPages) {
            endPageNumber = totalPages;
        }

        if (totalPages < 0) {
            totalPages = 1;
            endPageNumber = 1;
        }

        model.addAttribute("trainerName", trainerName);
        model.addAttribute("styleName", styleName);
        model.addAttribute("date", date);
        model.addAttribute("startPageNumber", pageNumber);
        model.addAttribute("endPageNumber", endPageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("additive", (pageNumber - 1) * pageSize + 1);
        model.addAttribute("booking", BookingDto.builder().build());
        model.addAttribute("lessons", lessonViewDtoPage.getContent());
    }

}
