package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.service.*;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final UserService userService;
    private final DanceStyleService danceStyleService;
    private final RoomService roomService;
    private final LessonPaginationService lessonPaginationService;
    private final SecurityContextFacade securityContextFacade;

    @Autowired
    public LessonController(final LessonService lessonService, final UserService userService,
                            final DanceStyleService danceStyleService, final RoomService roomService,
                            final LessonPaginationService lessonPaginationService,
                            final SecurityContextFacade securityContextFacade) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.danceStyleService = danceStyleService;
        this.roomService = roomService;
        this.lessonPaginationService = lessonPaginationService;
        this.securityContextFacade = securityContextFacade;
    }

    @PostMapping("/create")
    public String createLesson(final Model model, @ModelAttribute("lesson") @Valid final LessonDto lessonDto,
                               final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
                return "forms/trainer_lesson_form";
            } else {
                final List<UserViewDto> users = userService.listTrainers();
                model.addAttribute("trainers", users);
                return "forms/lesson_form";
            }
        }
        final int id = lessonService.createLesson(lessonDto);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/create")
    public String createLesson(final Model model) {
        prepareModel(model);
        model.addAttribute("lesson", LessonDto.builder().build());
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            return "forms/trainer_lesson_form";
        } else {
            final List<UserViewDto> users = userService.listTrainers();
            model.addAttribute("trainers", users);
            return "forms/lesson_form";
        }
    }

    @GetMapping("/{id}")
    public String getLesson(final Model model, @PathVariable final int id) {
        model.addAttribute("lesson_view", lessonService.getLessonViewById(id));
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(Role.ADMIN)) {
            return "infos/admin_lesson_info";
        } else {
            return "infos/lesson_info";
        }
    }

    @PutMapping("/{id}")
    public String updateLesson(final Model model, @ModelAttribute("lesson") @Valid final LessonDto lessonDto,
                               @PathVariable final int id, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
                return "forms/trainer_lesson_edit";
            } else {
                final List<UserViewDto> users = userService.listTrainers();
                model.addAttribute("trainers", users);
                return "forms/lesson_edit";
            }
        }
        lessonService.updateLessonById(lessonDto, id);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/{id}/update")
    public String updateLesson(final Model model, @PathVariable final int id) {
        prepareModel(model);
        model.addAttribute("lesson", lessonService.getLessonById(id));
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            return "forms/trainer_lesson_edit";
        } else {
            final List<UserViewDto> users = userService.listTrainers();
            model.addAttribute("trainers", users);
            return "forms/lesson_edit";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable final int id) {
        lessonService.deleteLessonById(id);
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            final Authentication authentication = securityContextFacade.getContext().getAuthentication();
            final String email = authentication.getName();
            final int userId = userService.getUserIdByEmail(email);
            return "redirect:/trainers/" + userId + "/lessons";
        }
        return "redirect:/lessons";
    }

    @GetMapping
    public String getLessons(@RequestParam(required = false) final Map<String, String> allParams, final Model model) {
        final FilteredLessonViewListPage filteredLessonViewListPage = lessonPaginationService
                .getFilteredLessonViewListPage(allParams.get("page"), allParams.get("size"),
                        allParams.get("trainerName"), allParams.get("styleName"), allParams.get("date"));

        model.addAttribute("lessonPage", filteredLessonViewListPage);
        model.addAttribute("booking", BookingDto.builder().build());
        model.addAttribute("styles", danceStyleService.listDanceStyleViews());

        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.ADMIN)) {
            return "lists/admin_lesson_list";
        } else {
            return "lists/user_lesson_list";
        }
    }

    private void prepareModel(final Model model) {
        final List<DanceStyleViewDto> styles = danceStyleService.listDanceStyleViews();
        final List<RoomViewDto> rooms = roomService.listRooms();
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
    }

}
