package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.service.DanceStyleService;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.RoomService;
import com.dataart.dancestudio.service.UserService;
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

    @Autowired
    public LessonController(final LessonService lessonService, final UserService userService,
                            final DanceStyleService danceStyleService, final RoomService roomService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.danceStyleService = danceStyleService;
        this.roomService = roomService;
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
    public String getLessons(final Model model) {
        model.addAttribute("booking", BookingDto.builder().build());
        model.addAttribute("lessons", lessonService.listLessons());
        return "lists/lesson_list";
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