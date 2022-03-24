package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.model.dto.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("booking", bookingService.getBookingById(id));
        bookingService.createBooking(bookingDto);
        return "infos/booking_info";
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

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable final int id) {
        bookingService.deleteBookingById(id);
    }

    @GetMapping("")
    public String getBookings(final Model model){
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "lists/booking_list";
    }

}
