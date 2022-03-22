package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserDto userDto) {
        service.createUser(userDto);
        return "redirect:/users";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", UserDto.builder().build());
        return "forms/user_form";
    }

    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable int id) {
        model.addAttribute("user", service.getUserById(id));
        return "infos/user_info";
    }

    @PutMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") UserDto userDto, @PathVariable int id) {
        service.updateUserById(userDto, id);
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String updateUser(Model model, @PathVariable int id) {
        model.addAttribute("user", service.getUserById(id));
        return "forms/user_edit";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        service.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", service.getAllUsers());
        return "lists/user_list";
    }

}
