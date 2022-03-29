package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
public class MainController {

    private final UserService userService;
    protected AuthenticationManager authenticationManager;

    @Autowired
    public MainController(final UserService userService, final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginUser(final Model model, final UserDto userDto) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        ) {
            return "redirect:/operations";
        }
        model.addAttribute("user", userDto);
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") final UserDto userDto) {
        userService.createUser(userDto);
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        final Authentication auth = authenticationManager.authenticate(token);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        return "redirect:/operations";
    }

    @GetMapping("/register")
    public String registerUser(final Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        ) {
            return "redirect:/operations";
        }
        model.addAttribute("user", UserDto.builder().build());
        return "registration";
    }

    @GetMapping("/operations")
    public String operations() {
        return "operations";
    }

}
