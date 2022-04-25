package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public String createBooking(final Model model, @ModelAttribute("booking") final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "redirect:/bookings/" + id;
    }

    @GetMapping("/{id}")
    public String getBooking(final Model model, @PathVariable final int id) {
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(final HttpSession session, @PathVariable final int id) {
        bookingService.deleteBookingById(id);
        return "redirect:/users/" + session.getAttribute("userId") + "/bookings";
    }

    @GetMapping
    public String getBookings(final Model model) {
        model.addAttribute("bookings", bookingService.listBookings());
        return "lists/booking_list";
    }

}
