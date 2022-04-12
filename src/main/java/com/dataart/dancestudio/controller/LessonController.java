package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.service.*;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final UserService userService;
    private final DanceStyleService danceStyleService;
    private final RoomService roomService;
    private final PaginationService paginationService;
    private final SecurityContextFacade securityContextFacade;

    @Autowired
    public LessonController(final LessonService lessonService, final UserService userService,
                            final DanceStyleService danceStyleService, final RoomService roomService,
                            final PaginationService paginationService, final SecurityContextFacade securityContextFacade) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.danceStyleService = danceStyleService;
        this.roomService = roomService;
        this.paginationService = paginationService;
        this.securityContextFacade = securityContextFacade;
    }

    @PostMapping("/create")
    public String createLesson(final Model model, @ModelAttribute("lesson") final LessonDto lessonDto) {
        final int id = lessonService.createLesson(lessonDto);
        model.addAttribute("lesson_view", lessonService.getLessonViewById(id));
        return "infos/lesson_info";
    }

    @GetMapping("/create")
    public String createLesson(final Model model) {
        prepareModel(model);
        model.addAttribute("lesson", LessonDto.builder().build());
        return "forms/lesson_form";
    }

    @GetMapping("/{id}")
    public String getLesson(final Model model, @PathVariable final int id) {
        model.addAttribute("lesson_view", lessonService.getLessonViewById(id));
        return "infos/lesson_info";
    }

    @PutMapping("/{id}")
    public String updateLesson(final Model model, @ModelAttribute("lesson") final LessonDto lessonDto, @PathVariable final int id) {
        lessonService.updateLessonById(lessonDto, id);
        model.addAttribute("lesson_view", lessonService.getLessonViewById(id));
        return "infos/lesson_info";
    }

    @GetMapping("/{id}/update")
    public String updateLesson(final Model model, @PathVariable final int id) {
        prepareModel(model);
        model.addAttribute("lesson", lessonService.getLessonById(id));
        return "forms/lesson_edit";
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable final int id) {
        lessonService.deleteLessonById(id);
        return "redirect:/lessons";
    }

    @GetMapping
    public String getLessons(@RequestParam(name = "page", required = false) final Integer page,
                             @RequestParam(name = "size", required = false) final Integer size,
                             @RequestParam(name = "trainerName", required = false) final String trainerName,
                             @RequestParam(name = "styleName", required = false) final String styleName,
                             @RequestParam(name = "date", required = false) final String date, final Model model) {
        final PageRequest pageRequest = paginationService.initPageRequest(page, size);
        final Page<LessonViewDto> lessonViewDtoPage = lessonService.listLessons(trainerName, styleName, date, pageRequest);
        prepareModel(model);
        prepareModelForLessons(trainerName, styleName, date, pageRequest.getPageNumber(), pageRequest.getPageSize(), lessonViewDtoPage, model);

        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.USER)) {
            return "lists/user_lesson_list";
        } else {
            return "lists/lesson_list";
        }
    }

    private void prepareModel(final Model model) {
        final List<UserViewDto> users = userService.listTrainers();
        final List<DanceStyleViewDto> styles = danceStyleService.listDanceStyleViews();
        final List<RoomViewDto> rooms = roomService.listRooms();
        model.addAttribute("trainers", users);
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
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
