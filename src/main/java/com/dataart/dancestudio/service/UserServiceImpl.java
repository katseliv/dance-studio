package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                           final UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public int createUser(final UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isEmpty()) {
            final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
            return userRepository.save(userMapper.userRegistrationDtoToUserRegistrationEntityWithPassword(userRegistrationDto, password));
        }
        throw new UserAlreadyExistsException("User already exists in the database!");
    }

    @Override
    public UserDto getUserById(final int id) {
        return userMapper.userEntityToUserDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserViewDto getUserViewById(final int id) {
        return userMapper.userEntityToUserViewDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public int getUserIdByEmail(final String email) {
        final UserDetailsDto userDetailsDto = userMapper.userDetailsEntityToUserDetailsDto(
                userRepository.findByEmail(email).orElseThrow()
        );
        return userDetailsDto.getId();
    }

    @Override
    public void updateUserById(final UserDto userDto, final int id) {
        try {
            if (!userDto.getMultipartFile().isEmpty()) {
                userRepository.update(userMapper.userDtoToUserEntity(userDto), id);
            } else {
                final UserDto userDtoFromDB = getUserById(id);
                if (!hasToBeUpdated(userDto, userDtoFromDB)) {
                    userRepository.updateWithoutPicture(userMapper.userDtoToUserEntity(userDto), id);
                }
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void deleteUserById(final int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserViewDto> listUsers() {
        return userMapper.userEntitiesToUserViewDtoList(userRepository.findAll());
    }

    @Override
    public List<UserViewDto> listTrainers() {
        return userMapper.userEntitiesToUserViewDtoList(userRepository.findAllByRole(Role.TRAINER));
    }

    //TODO: think about naming
    private boolean hasToBeUpdated(final UserDto userDto, final UserDto userDtoFromDB) {
        return Objects.equals(userDto.getFirstName(), userDtoFromDB.getFirstName()) &&
                Objects.equals(userDto.getLastName(), userDtoFromDB.getLastName()) &&
                Objects.equals(userDto.getEmail(), userDtoFromDB.getEmail()) &&
                Objects.equals(userDto.getPhoneNumber(), userDtoFromDB.getPhoneNumber());
    }

}
