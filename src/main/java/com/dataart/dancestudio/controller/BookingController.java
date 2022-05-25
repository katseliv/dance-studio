package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PaginationService<BookingViewDto> bookingPaginationService;

    @PostMapping("/create")
    public String createBooking(@ModelAttribute("booking") final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        return "redirect:/bookings/" + id;
    }

    @GetMapping("/{id}")
    public String getBooking(@PathVariable final int id, final Model model) {
        model.addAttribute("booking_view", bookingService.getBookingViewById(id));
        return "infos/booking_info";
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable final int id, final HttpSession session) {
        bookingService.deleteBookingById(id);
        return "redirect:/users/" + session.getAttribute("userId") + "/bookings";
    }

    @GetMapping
    public String getBookings(@RequestParam(required = false) final Map<String, String> allParams, final Model model) {
        final ViewListPage<BookingViewDto> bookingViewListPage = bookingPaginationService
                .getViewListPage(allParams.get("page"), allParams.get("size"));
        model.addAttribute("bookings", bookingViewListPage.getViewDtoList());
        return "lists/booking_list";
    }

}
