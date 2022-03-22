package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.BookingService;
import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.model.BookingDto;
import com.dataart.dancestudio.service.model.view.LessonViewDto;
import com.dataart.dancestudio.service.model.view.UserViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;
    private final UserService userService;
    private final LessonService lessonService;

    @Autowired
    public BookingController(BookingService service, UserService userService, LessonService lessonService) {
        this.service = service;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("booking") BookingDto bookingDto) {
        service.createBooking(bookingDto);
        return "redirect:/bookings";
    }

    @GetMapping("/create")
    public String createBooking(Model model) {
        List<UserViewDto> users = userService.getAllUsers();
        List<LessonViewDto> lessons = lessonService.getAllLessons();
        model.addAttribute("users", users);
        model.addAttribute("lessons", lessons);
        model.addAttribute("booking", BookingDto.builder().build());
        return "forms/booking_form";
    }

    @GetMapping("/{id}")
    public String getBooking(Model model, @PathVariable int id) {
        model.addAttribute("booking", service.getBookingViewById(id));
        return "infos/booking_info";
    }

    @PutMapping("/update/{id}")
    public String updateBooking(@ModelAttribute("booking") BookingDto bookingDto, @PathVariable int id) {
        service.updateBookingById(bookingDto, id);
        return "redirect:/bookings";
    }

    @GetMapping("/update/{id}")
    public String updateBooking(Model model, @PathVariable int id) {
        List<UserViewDto> users = userService.getAllUsers();
        List<LessonViewDto> lessons = lessonService.getAllLessons();
        model.addAttribute("users", users);
        model.addAttribute("lessons", lessons);
        model.addAttribute("booking", service.getBookingById(id));
        return "forms/booking_edit";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id) {
        service.deleteBookingById(id);
        return "redirect:/bookings";
    }

    @GetMapping
    public String getBookings(Model model){
        model.addAttribute("bookings", service.getAllBookings());
        return "lists/booking_list";
    }

}
