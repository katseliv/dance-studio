package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.logic.LessonService;
import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.model.dto.BookingDto;
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

    private final BookingService bookingService;
    private final UserService userService;
    private final LessonService lessonService;

    @Autowired
    public BookingController(final BookingService bookingService, UserService userService, LessonService lessonService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    public String createBooking(final Model model, @ModelAttribute("booking") final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        model.addAttribute("booking", bookingService.getBookingById(id));
        bookingService.createBooking(bookingDto);
        return "infos/booking_info";
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
    public String getBooking(final Model model, @PathVariable final int id) {
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "infos/booking_info";
    }

    @PutMapping("/{id}")
    public String updateBooking(@RequestBody final BookingDto bookingDto, @PathVariable final int id) {
        bookingService.updateBookingById(bookingDto, id);
        return "infos/booking_info";
    }

    @GetMapping("/{id}/update")
    public String updateBooking(Model model, @PathVariable int id) {
        List<UserViewDto> users = userService.getAllUsers();
        List<LessonViewDto> lessons = lessonService.getAllLessons();
        model.addAttribute("users", users);
        model.addAttribute("lessons", lessons);
        model.addAttribute("booking", service.getBookingById(id));
        return "forms/booking_edit";
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable final int id) {
        bookingService.deleteBookingById(id);
    }

    @GetMapping("")
    public String getBookings(final Model model){
        model.addAttribute("bookings", bookingService.listBookings());
        return "lists/booking_list";
    }

}
