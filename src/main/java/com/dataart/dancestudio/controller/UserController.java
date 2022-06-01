package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.BookingService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final SecurityContextFacade securityContextFacade;
    protected AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String loginUser(final Model model) {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        model.addAttribute("user", UserDetailsDto.builder().build());
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid final UserRegistrationDto userRegistrationDto,
                               final Model model, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
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
    public String createUser(@ModelAttribute("user") @Valid final UserRegistrationDto userRegistrationDto,
                             final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "forms/user_form";
        }
        final int id = userService.createUser(userRegistrationDto);
        return "redirect:/users/" + id;
    }

    @GetMapping("/create")
    public String createUser(final Model model) {
        model.addAttribute("user", UserRegistrationDto.builder().build());
        return "forms/user_form";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable final int id, final Model model) {
        model.addAttribute("user_view", userService.getUserViewById(id));
        return "infos/user_info";
    }

    @PutMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid final UserDto userDto, @PathVariable final int id,
                             final Model model, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/user_edit";
        }
        userService.updateUserById(userDto, id);
        model.addAttribute("user_view", userService.getUserViewById(id));
        return "infos/user_info";
    }

    @GetMapping("/{id}/update")
    public String updateUser(@PathVariable final int id, final Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "forms/user_edit";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable final int id) {
        try {
            userService.deleteUserById(id);
        } catch (final UserCanNotBeDeletedException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/users";
    }

    @GetMapping
    public String getUsers(@RequestParam(required = false) final Map<String, String> allParams, final Model model) {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        final String email = authentication.getName();

        final ViewListPage<UserForListDto> userForListDtoViewListPage = userService
                .getViewListPage(allParams.get("page"), allParams.get("size"));
        model.addAttribute("id", userService.getUserIdByEmail(email));
        model.addAttribute("users", userForListDtoViewListPage.getViewDtoList());
        return "lists/user_list";
    }

    @GetMapping("/{id}/bookings")
    public String getUserBookings(@RequestParam(required = false) final Map<String, String> allParams,
                                  @PathVariable final int id, final Model model) {
        final ViewListPage<BookingViewDto> bookingViewDtoViewListPage = bookingService
                .getUserViewListPage(id, allParams.get("page"), allParams.get("size"));
        model.addAttribute("bookings", bookingViewDtoViewListPage.getViewDtoList());
        return "lists/booking_list";
    }

    private boolean isAuthenticated() {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

}
