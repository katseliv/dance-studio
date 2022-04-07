package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final SecurityContextFacade securityContextFacade;

    @Autowired
    public BookingController(final BookingService bookingService, final UserService userService,
                             final SecurityContextFacade securityContextFacade) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.securityContextFacade = securityContextFacade;
    }

    @PostMapping("/create")
    public String createBooking(final Model model, @ModelAttribute("booking") final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
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

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable final int id) {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        final String email = authentication.getName();
        bookingService.deleteBookingById(id);
        return "redirect:/users/" + userService.getUserIdByEmail(email) + "/bookings";
    }

    @GetMapping
    public String getBookings(final Model model) {
        model.addAttribute("bookings", bookingService.listBookings());
        return "lists/booking_list";
    }

}
