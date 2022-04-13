package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
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
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final SecurityContextFacade securityContextFacade;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public UserController(final UserService userService, final BookingService bookingService,
                          final SecurityContextFacade securityContextFacade, final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.securityContextFacade = securityContextFacade;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginUser(final Model model) {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("user", UserDetailsDto.builder().build());
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(final Model model, @ModelAttribute("user") final UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException {
        if (!Objects.equals(userRegistrationDto.getPassword(), userRegistrationDto.getPasswordConfirmation())) {
            model.addAttribute("user", userRegistrationDto);
            model.addAttribute("error", "Password wasn't confirmed");
            return "registration";
        }
        final int id = userService.createUser(userRegistrationDto);
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userRegistrationDto.getEmail(), userRegistrationDto.getPassword());
        final Authentication auth = authenticationManager.authenticate(token);
        final SecurityContext securityContext = securityContextFacade.getContext();
        securityContext.setAuthentication(auth);
        return "redirect:/users/" + id;
    }

    @GetMapping("/register")
    public String registerUser(final Model model) {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("user", UserRegistrationDto.builder().build());
        return "registration";
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

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable final int id) {
        try {
            userService.deleteUserById(id);
        } catch (final UserCanNotBeDeletedException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        return "redirect:/users";
    }

    @GetMapping
    public String getUsers(final Model model) {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        final String email = authentication.getName();
        model.addAttribute("id", userService.getUserIdByEmail(email));
        model.addAttribute("users", userService.listUsers());
        return "lists/user_list";
    }

    @GetMapping("/{id}/bookings")
    public String getUserBookings(final Model model, @PathVariable final int id) {
        model.addAttribute("bookings", bookingService.listUserBookings(id));
        return "lists/booking_list";
    }

    private boolean isAuthenticated() {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
