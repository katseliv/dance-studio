package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.BookingService;
import com.dataart.dancestudio.service.model.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public String createBooking(Model model, @ModelAttribute("booking") BookingDto bookingDto) {
        service.createBooking(bookingDto);
        return "booking_form";
    }

    @GetMapping("/{id}")
    public String getBooking(Model model, @PathVariable int id) {
        model.addAttribute("booking", service.getBookingById(id));
        return "booking_info";
    }

    @PutMapping("/update/{id}")
    public void updateBooking(@RequestBody BookingDto bookingDto, @PathVariable int id) {
        service.updateBookingById(bookingDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable int id) {
        service.deleteBookingById(id);
    }

    @GetMapping("/")
    public String getBookings(Model model){
        model.addAttribute("bookings", service.getAllBookings());
        return "booking_list";
    }

}
