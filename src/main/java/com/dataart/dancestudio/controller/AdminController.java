package com.dataart.dancestudio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @GetMapping("/operations")
    public String operations() {
        return "operations/operations";
    }

}
