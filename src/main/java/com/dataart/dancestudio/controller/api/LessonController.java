package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.DanceStyleService;
import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.logic.RoomService;
import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.model.*;
import com.dataart.dancestudio.service.model.view.DanceStyleViewDto;
import com.dataart.dancestudio.service.model.view.RoomViewDto;
import com.dataart.dancestudio.service.model.view.UserViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService service;
    private final UserService userService;
    private final DanceStyleService danceStyleService;
    private final RoomService roomService;

    @Autowired
    public LessonController(LessonService service, UserService userService,
                            DanceStyleService danceStyleService, RoomService roomService) {
        this.service = service;
        this.userService = userService;
        this.danceStyleService = danceStyleService;
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public String createLesson(@ModelAttribute("lesson") LessonDto lessonDto) {
        service.createLesson(lessonDto);
        return "redirect:/lessons";
    }

    @GetMapping("/create")
    public String createLesson(Model model) {
        List<UserViewDto> users = userService.getAllUsers();
        List<DanceStyleViewDto> styles = danceStyleService.getAllDanceStyles();
        List<RoomViewDto> rooms = roomService.getAllRooms();
        model.addAttribute("trainers", users);
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
        model.addAttribute("lesson", LessonDto.builder().build());
        return "forms/lesson_form";
    }

    @GetMapping("/{id}")
    public String getLesson(Model model, @PathVariable int id) {
        model.addAttribute("lesson", service.getLessonViewById(id));
        return "infos/lesson_info";
    }

    @PutMapping("/update/{id}")
    public String updateLesson(@ModelAttribute("lesson") LessonDto lessonDto, @PathVariable int id) {
        service.updateLessonById(lessonDto, id);
        return "redirect:/lessons";
    }

    @GetMapping("/update/{id}")
    public String updateLesson(Model model, @PathVariable int id) {
        List<UserViewDto> users = userService.getAllUsers();
        List<DanceStyleViewDto> styles = danceStyleService.getAllDanceStyles();
        List<RoomViewDto> rooms = roomService.getAllRooms();
        model.addAttribute("trainers", users);
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
        model.addAttribute("lesson", service.getLessonById(id));
        return "forms/lesson_edit";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLesson(@PathVariable int id) {
        service.deleteLessonById(id);
        return "redirect:/lessons";
    }

    @GetMapping
    public String getLessons(Model model) {
        model.addAttribute("lessons", service.getAllLessons());
        return "lists/lesson_list";
    }

}
