package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LessonService lessonService;

    @Autowired
    public UserServiceImpl(final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                           final UserMapper userMapper, final LessonService lessonService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.lessonService = lessonService;
    }

    @Override
    public int createUser(final UserRegistrationDto userRegistrationDto) throws IOException, UserAlreadyExistsException {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isEmpty()) {
            final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
            return userRepository.save(
                    userMapper.userRegistrationDtoToUserRegistrationEntityWithPassword(userRegistrationDto, password));
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
                if (!isAnyUserProfileDataUpdated(userDto, userDtoFromDB)) {
                    userRepository.updateWithoutPicture(userMapper.userDtoToUserEntity(userDto), id);
                }
            }
        } catch (final IOException e) {
            log.error("An exception occurred!", e);
        }
    }

    @Override
    public void deleteUserById(final int id) throws UserCanNotBeDeletedException {
        if (lessonService.numberOfUserLessons(id) == 0) {
            userRepository.markAsDeleted(id);
        } else {
            throw new UserCanNotBeDeletedException("User has lessons!!!");
        }
    }

    @Override
    public List<UserViewDto> listUsers() {
        return userMapper.userEntitiesToUserViewDtoList(userRepository.findAll());
    }

    @Override
    public List<UserViewDto> listTrainers() {
        return userMapper.userEntitiesToUserViewDtoList(userRepository.findAllByRole(Role.TRAINER));
    }

    private boolean isAnyUserProfileDataUpdated(final UserDto userDto, final UserDto userDtoFromDB) {
        return Objects.equals(userDto.getFirstName(), userDtoFromDB.getFirstName()) &&
                Objects.equals(userDto.getLastName(), userDtoFromDB.getLastName()) &&
                Objects.equals(userDto.getEmail(), userDtoFromDB.getEmail()) &&
                Objects.equals(userDto.getPhoneNumber(), userDtoFromDB.getPhoneNumber());
    }

}
