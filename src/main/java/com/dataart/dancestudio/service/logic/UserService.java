package com.dataart.dancestudio.service.logic;

import com.dataart.dancestudio.service.model.UserDto;

import java.util.List;

public interface UserService {

    void createUser(UserDto userDto);

    UserDto getUserById(int id);

    void updateUserById(UserDto userDto, int id);

    void deleteUserById(int id);

    List<UserDto> getAllUsers();

}
