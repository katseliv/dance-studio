package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;

import java.util.List;

public interface UserService {

    int createUser(UserDto userDto);

    UserDto getUserById(int id);

    void updateUserById(UserDto userDto, int id);

    void deleteUserById(int id);

    List<UserViewDto> getAllUsers();

}
