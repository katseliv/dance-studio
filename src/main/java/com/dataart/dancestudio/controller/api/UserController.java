package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createUser(final Model model, @ModelAttribute("user") final UserDto userDto) {
        final int id = userService.createUser(userDto);
        model.addAttribute("user", userService.getUserById(id));
        return "infos/user_info";
    }

    @GetMapping("/create")
    public String createUser(final Model model) {
        model.addAttribute("user", UserDto.builder().build());
        return "forms/user_form";
    }

    @GetMapping("/{id}")
    public String getUser(final Model model, @PathVariable final int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "infos/user_info";
    }

    @PutMapping("/{id}")
    public String updateUser(@ModelAttribute("user") final UserDto userDto, @PathVariable final int id) {
        userService.updateUserById(userDto, id);
        return "infos/user_info";
    }

    @GetMapping("/{id}/update")
    public String updateUser(final Model model, @PathVariable final int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "forms/user_edit";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable final int id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping
    public String getUsers(final Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "lists/user_list";
    }

}
