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
    private final LessonPaginationService lessonPaginationService;
    private final SecurityContextFacade securityContextFacade;

    @Autowired
    public LessonController(final LessonService lessonService, final UserService userService,
                            final DanceStyleService danceStyleService, final RoomService roomService,
                            final LessonPaginationService lessonPaginationService, final SecurityContextFacade securityContextFacade) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.danceStyleService = danceStyleService;
        this.roomService = roomService;
        this.lessonPaginationService = lessonPaginationService;
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
        final FilteredLessonViewListPage filteredLessonViewListPage = lessonPaginationService.getFilteredLessonViewListPage(page, size, trainerName, styleName, date);

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
        final List<UserViewDto> users = userService.listTrainers();
        final List<DanceStyleViewDto> styles = danceStyleService.listDanceStyleViews();
        final List<RoomViewDto> rooms = roomService.listRooms();
        model.addAttribute("trainers", users);
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
    }

}
