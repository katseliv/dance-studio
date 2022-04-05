package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.ContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final ContextFacade contextFacade;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public UserController(final UserService userService, final BookingService bookingService,
                          final ContextFacade contextFacade, final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.contextFacade = contextFacade;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginUser(final Model model) {
        if (isAuthenticated()) {
            return "redirect:/users/operations";
        }
        model.addAttribute("user", UserDetailsDto.builder().build());
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") final UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException {
        final int id = userService.createUser(userRegistrationDto);
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userRegistrationDto.getEmail(), userRegistrationDto.getPassword());
        final Authentication auth = authenticationManager.authenticate(token);
        final SecurityContext securityContext = contextFacade.getContext();
        securityContext.setAuthentication(auth);
        return "redirect:/users/" + id;
    }

    @GetMapping("/register")
    public String registerUser(final Model model) {
        if (isAuthenticated()) {
            return "redirect:/users/operations";
        }
        model.addAttribute("user", UserRegistrationDto.builder().build());
        return "registration";
    }

    private boolean isAuthenticated() {
        final Authentication authentication = contextFacade.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/operations")
    public String getUserOperations(final Model model) {
        final String email = contextFacade.getContext().getAuthentication().getName();
        model.addAttribute("id", userService.getUserIdByEmail(email));
        return "operations/user_operations";
    }

    @PostMapping("/create")
    public String createUser(final Model model, @ModelAttribute("user") final UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException {
        final int id = userService.createUser(userRegistrationDto);
        model.addAttribute("user_view", userService.getUserViewById(id));
        return "infos/user_info";
    }

    @GetMapping("/create")
    public String createUser(final Model model) {
        model.addAttribute("user", UserRegistrationDto.builder().build());
        return "forms/user_form";
    }

    @GetMapping("/{id}")
    public String getUser(final Model model, @PathVariable final int id) {
        model.addAttribute("user_view", userService.getUserViewById(id));
        return "infos/user_info";
    }

    @PutMapping("/{id}")
    public String updateUser(final Model model, @ModelAttribute("user") final UserDto userDto, @PathVariable final int id) {
        userService.updateUserById(userDto, id);
        model.addAttribute("user_view", userService.getUserViewById(id));
        return "infos/user_info";
    }

    @GetMapping("/{id}/update")
    public String updateUser(final Model model, @PathVariable final int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "forms/user_edit";
    }

    @DeleteMapping("/{id}") //admin
    public String deleteUser(@PathVariable final int id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping
    public String getUsers(final Model model) {
        model.addAttribute("users", userService.listUsers());
        return "lists/user_list";
    }

    @GetMapping("/{id}/bookings")
    public String getUserBookings(final Model model, @PathVariable final int id) {
        model.addAttribute("bookings", bookingService.listUserBookings(id));
        return "lists/booking_list";
    }

}
