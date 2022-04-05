package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

    int createUser(UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException;

    UserDto getUserById(int id);

    UserViewDto getUserViewById(int id);

    int getUserIdByEmail(String email);

    void updateUserById(UserDto userDto, int id);

    void deleteUserById(int id);

    List<UserViewDto> listUsers();

}
