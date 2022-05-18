package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingRestController {

    private final BookingService bookingService;

    @Autowired
    public BookingRestController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Integer> createBooking(@RequestBody final BookingDto bookingDto) {
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
    public List<BookingViewDto> getBookings() {
        return bookingService.listBookings();
    }

}
