package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.UserAlreadyExistsException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    public int createUser(final UserRegistrationDto userRegistrationDto) throws IOException {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isEmpty()) {
            final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
            final UserEntity userEntity = userMapper.userRegistrationDtoToUserEntityWithPassword(
                    userRegistrationDto, password);
            userEntity.setRole(Role.USER);
            final UserEntity newUserEntity = userRepository.save(userEntity);
            final Integer id = newUserEntity.getId();
            log.info("User with id = {} was created.", id);
            return id;
        }
        log.info("User wasn't created.");
        throw new UserAlreadyExistsException("User already exists in the database!");
    }

    @Override
    public UserDto getUserById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} was found.", user.getId()),
                () -> log.info("User wasn't found."));
        return userMapper.userEntityToUserDto(userEntity.orElseThrow());
    }

    @Override
    public UserViewDto getUserViewById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} was found.", user.getId()),
                () -> log.info("User wasn't found."));
        return userMapper.userEntityToUserViewDto(userEntity.orElseThrow());
    }

    @Override
    public int getUserIdByEmail(final String email) {
        final UserDetailsDto userDetailsDto = userMapper.userEntityToUserDetailsDto(
                userRepository.findByEmail(email).orElseThrow());
        final Integer id = userDetailsDto.getId();
        log.info("User with id = {} was found.", id);
        return id;
    }

    @Override
    public void updateUserById(final UserDto userDto, final int id) {
        try {
            final UserEntity userEntity = userRepository.findById(id).orElseThrow();
            if (!userDto.getMultipartFile().isEmpty()) {
                userMapper.mergeUserEntityAndUserDto(userEntity, userDto);
                userRepository.save(userEntity);
                log.info("User with id = {} was updated with picture.", id);
            } else {
                userMapper.mergeUserEntityAndUserDtoWithoutPicture(userEntity, userDto);
                userRepository.save(userEntity);
                log.info("User with id = {} was updated without picture.", id);
            }
        } catch (final IOException e) {
            log.error("An exception occurred!", e);
        }
    }

    @Override
    public void deleteUserById(final int id) {
        if (lessonService.numberOfUserLessons(id) == 0) {
            userRepository.markAsDeletedById(id);
            log.info("User with id = {} was deleted.", id);
        } else {
            log.info("User with id = {} wasn't deleted.", id);
            throw new UserCanNotBeDeletedException("User has lessons!!!");
        }
    }

    @Override
    public List<UserViewDto> listUsers() {
        final List<UserEntity> userEntities = userRepository.findAll();
        if (userEntities.size() != 0) {
            log.info("Users were found.");
        } else {
            log.info("There weren't users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    public List<UserViewDto> listTrainers() {
        final List<UserEntity> userEntities = userRepository.findAllByRole(Role.TRAINER);
        if (userEntities.size() != 0) {
            log.info("Users were found.");
        } else {
            log.info("There weren't users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

}
