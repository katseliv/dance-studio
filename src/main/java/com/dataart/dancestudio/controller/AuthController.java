package com.dataart.dancestudio.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping()
    public String loginUser() {
        return "login";
    }

}
