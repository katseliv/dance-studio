package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.DanceStyleService;
import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.logic.RoomService;
import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.model.DanceStyleDto;
import com.dataart.dancestudio.service.model.LessonDto;
import com.dataart.dancestudio.service.model.RoomDto;
import com.dataart.dancestudio.service.model.UserDto;
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
    public String createLesson(Model model, @ModelAttribute("lesson") LessonDto lessonDto) {
        service.createLesson(lessonDto);
        return "lesson_form";
    }

    @GetMapping("/create")
    public String getLessonForm(Model model) {
        model.addAttribute("lesson", new LessonDto());
        List<UserDto> users = userService.getAllUsers();
        List<DanceStyleDto> styles = danceStyleService.getAllDanceStyles();
        List<RoomDto> rooms = roomService.getAllRooms();
        model.addAttribute("trainers", users);
        model.addAttribute("styles", styles);
        model.addAttribute("rooms", rooms);
        return "lesson_form";
    }

    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable int id) {
        return service.getLessonById(id);
    }

    @PutMapping("/update/{id}")
    public void updateLesson(@RequestBody LessonDto lessonDto, @PathVariable int id) {
        service.updateLessonById(lessonDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLesson(@PathVariable int id) {
        service.deleteLessonById(id);
    }

    @GetMapping("/")
    public List<LessonDto> getLessons() {
        return service.getAllLessons();
    }

}
