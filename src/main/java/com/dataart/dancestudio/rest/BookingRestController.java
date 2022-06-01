package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRestController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Integer> createBooking(@RequestBody @Valid final BookingDto bookingDto) {
        final int id = bookingService.createBooking(bookingDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public BookingViewDto getBooking(@PathVariable final int id) {
        return bookingService.getBookingViewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable final int id) {
        bookingService.deleteBookingById(id);
    }

    @GetMapping
    public ViewListPage<BookingViewDto> getBookings(@RequestParam(required = false) final Map<String, String> allParams) {
        return bookingService.getViewListPage(allParams.get("page"), allParams.get("size"));
    }

}
