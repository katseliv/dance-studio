package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class MainController {

    private final UserService userService;
    private final SecurityContextFacade securityContextFacade;

    public MainController(final UserService userService, final SecurityContextFacade securityContextFacade) {
        this.userService = userService;
        this.securityContextFacade = securityContextFacade;
    }

    @GetMapping("/")
    public String index() {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String operations(final HttpSession session) {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        final String email = authentication.getName();
        final int userId = userService.getUserIdByEmail(email);
        session.setAttribute("userId", userId);
        session.setAttribute("roleId", userService.getUserById(userId).getRoleId());
        return "home";
    }

    private boolean isAuthenticated() {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

}
