package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.model.Provider;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    int createUser(UserRegistrationDto userRegistrationDto, Provider provider) throws EntityAlreadyExistsException;

    UserDto getUserById(int id);

    UserViewDto getUserViewById(int id);

    UserDetailsDto getUserDetailsById(int id);

    int getUserIdByEmail(String email);

    UserDetailsDto getUserByEmail(String email);

    boolean existsByUserEmail(String email);

    void updateUserById(int id, UserDto userDto);

    void deleteUserById(int id) throws UserCanNotBeDeletedException;

    ViewListPage<UserForListDto> getViewListPage(String page, String size);

    List<UserForListDto> listTrainers(Pageable pageable);

    List<UserForListDto> listUsers(Pageable pageable);

    int numberOfUsers();
}
