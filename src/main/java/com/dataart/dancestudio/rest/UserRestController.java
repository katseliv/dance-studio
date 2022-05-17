package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public UserRestController(final UserService userService, final BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @PostMapping("/register")
    public int register(@RequestBody @Valid final UserRegistrationDto userRegistrationDto) throws IOException {
        return userService.createUser(userRegistrationDto);
    }

    @PostMapping
    public int createUser(@RequestBody @Valid final UserRegistrationDto userRegistrationDto) throws IOException {
        return userService.createUser(userRegistrationDto);
    }

    @GetMapping("/{id}")
    public UserViewDto getUser(@PathVariable final int id) {
        return userService.getUserViewById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid final UserDto userDto, @PathVariable final int id) {
        userService.updateUserById(userDto, id);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable final int id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/{id}/bookings")
    public List<BookingViewDto> getUserBookings(@PathVariable final int id) {
        return bookingService.listUserBookings(id);
    }

    @GetMapping
    public List<UserViewDto> getUsers() {
        return userService.listUsers();
    }

}
