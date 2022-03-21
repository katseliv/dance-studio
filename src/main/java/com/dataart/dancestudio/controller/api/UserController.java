package com.dataart.dancestudio.controller.api;

import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public String createUser(Model model, @ModelAttribute("user") UserDto userDto) {
        service.createUser(userDto);
        return "user_form";
    }

    @GetMapping("/create")
    public String getUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user_form";
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        return service.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public void updateUser(@RequestBody UserDto userDto, @PathVariable int id) {
        service.updateUserById(userDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteUserById(id);
    }

    @GetMapping("/")
    public List<UserDto> getUsers(){
        return service.getAllUsers();
    }

}
