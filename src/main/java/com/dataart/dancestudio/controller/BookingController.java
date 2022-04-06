package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.UserService;
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
    public BookingController(final BookingService bookingService, final UserService userService,
                             final LessonService lessonService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    public String createBooking(final Model model, @ModelAttribute("booking") final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
    }

    @GetMapping("/create")
    public String createBooking(final Model model) {
        prepareModel(model);
        model.addAttribute("booking", BookingDto.builder().build());
        return "forms/booking_form";
    }

    @GetMapping("/{id}")
    public String getBooking(final Model model, @PathVariable final int id) {
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
    }

    @PutMapping("/{id}")
    public String updateBooking(final Model model, @ModelAttribute("booking") final BookingDto bookingDto, @PathVariable final int id) {
        bookingService.updateBookingById(bookingDto, id);
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
    }

    @GetMapping("/{id}/update")
    public String updateBooking(final Model model, @PathVariable final int id) {
        prepareModel(model);
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "forms/booking_edit";
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable final int id) {
        bookingService.deleteBookingById(id);
        return "redirect:/bookings";
    }

    @GetMapping
    public String getBookings(final Model model) {
        model.addAttribute("bookings", bookingService.listBookings());
        return "lists/booking_list";
    }

    private void prepareModel(final Model model) {
        final List<UserViewDto> users = userService.listUsers();
        final List<LessonViewDto> lessons = lessonService.listLessons();
        model.addAttribute("user_views", users);
        model.addAttribute("lesson_views", lessons);
    }

}
