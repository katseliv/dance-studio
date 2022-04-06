package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.exception.TargetUrlNotFound;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping
public class MainController {

    private final UserService userService;
    private final SecurityContextFacade securityContextFacade;
    private static final Map<String, String> authorityToTargetUrlMap = Map.of(
            Role.USER.getAuthority(), "operations/user_operations",
            Role.TRAINER.getAuthority(), "operations/trainer_operations",
            Role.ADMIN.getAuthority(), "operations/admin_operations"
    );

    public MainController(final UserService userService, final SecurityContextFacade securityContextFacade) {
        this.userService = userService;
        this.securityContextFacade = securityContextFacade;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/operations")
    public String operations(final Model model) throws TargetUrlNotFound {
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        final String email = authentication.getName();
        model.addAttribute("id", userService.getUserIdByEmail(email));
        return determineTargetUrl(authentication);
    }

    protected String determineTargetUrl(final Authentication authentication) throws TargetUrlNotFound {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authorityToTargetUrlMap::get)
                .findFirst()
                .orElseThrow(() -> new TargetUrlNotFound("There is not appropriate target url for this role!"));
    }

}
