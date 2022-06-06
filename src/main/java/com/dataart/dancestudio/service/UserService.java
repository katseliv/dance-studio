package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    int createUser(UserRegistrationDto userRegistrationDto) throws EntityAlreadyExistsException;

    UserDto getUserById(int id);

    UserViewDto getUserViewById(int id);

    int getUserIdByEmail(String email);

    UserDetailsDto getUserByEmail(final String email);

    void updateUserById(UserDto userDto, int id);

    void deleteUserById(int id) throws UserCanNotBeDeletedException;

    ViewListPage<UserForListDto> getViewListPage(String page, String size);

    List<UserForListDto> listTrainers(Pageable pageable);

    List<UserForListDto> listUsers(Pageable pageable);

    int numberOfUsers();

}
