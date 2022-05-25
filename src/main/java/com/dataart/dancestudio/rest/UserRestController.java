package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.PaginationService;
import com.dataart.dancestudio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final PaginationService<UserForListDto> userPaginationService;
    private final PaginationService<BookingViewDto> bookingPaginationService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody @Valid final UserRegistrationDto userRegistrationDto) {
        final int id = userService.createUser(userRegistrationDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Integer> createUser(@RequestBody @Valid final UserRegistrationDto userRegistrationDto) {
        final int id = userService.createUser(userRegistrationDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public UserViewDto getUser(@PathVariable final int id) {
        return userService.getUserViewById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid final UserDto userDto, @PathVariable final int id) {
        userService.updateUserById(userDto, id);
        return new ResponseEntity<>("User was updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable final int id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/{id}/bookings")
    public ViewListPage<BookingViewDto> getUserBookings(@RequestParam(required = false) final Map<String, String> allParams,
                                                        @PathVariable final int id) {
        return bookingPaginationService.getUserViewListPage(id, allParams.get("page"), allParams.get("size"));
    }

    @GetMapping
    public ViewListPage<UserForListDto> getUsers(@RequestParam(required = false) final Map<String, String> allParams) {
        return userPaginationService.getViewListPage(allParams.get("page"), allParams.get("size"));
    }

}
