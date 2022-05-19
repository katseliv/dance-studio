package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.mapper.UserMapper;
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
    public int createUser(final UserRegistrationDto userRegistrationDto) throws EntityAlreadyExistsException {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isEmpty()) {
            final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
            final UserEntity userEntity = userMapper.userRegistrationDtoToUserEntityWithPassword(
                    userRegistrationDto, password);
            userEntity.setImage(new byte[0]);
            userEntity.setRole(Role.USER);

            final UserEntity newUserEntity = userRepository.save(userEntity);
            final Integer id = newUserEntity.getId();
            log.info("User with id = {} has been created.", id);
            return id;
        }
        log.warn("User hasn't been created.");
        throw new EntityAlreadyExistsException("User already exists in the database!");
    }

    @Override
    public UserDto getUserById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", user.getId()),
                () -> log.warn("User with id = {} hasn't been found.", id));
        return userEntity.map(userMapper::userEntityToUserDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public UserViewDto getUserViewById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", user.getId()),
                () -> log.warn("User with id = {} hasn't been found.", id));
        return userEntity.map(userMapper::userEntityToUserViewDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public int getUserIdByEmail(final String email) {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with email = {} has been found.", email),
                () -> log.warn("User with email = {} hasn't been found.", email));
        return userEntity.map(userMapper::userEntityToUserDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!")).getId();
    }

    @Override
    public void updateUserById(final UserDto userDto, final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        final UserEntity user;
        if (userEntity.isPresent()) {
            user = userEntity.get();
            log.info("User with id = {} has been found.", id);
        } else {
            log.warn("User with id = {} hasn't been found.", id);
            throw new EntityNotFoundException("User not found!");
        }

        if (userDto.getBase64StringImage().isEmpty()) {
            userMapper.mergeUserEntityAndUserDtoWithoutPicture(user, userDto);
            userRepository.save(user);
            log.info("User with id = {} has been updated without picture.", id);
        } else {
            userMapper.mergeUserEntityAndUserDto(user, userDto);
            userRepository.save(user);
            log.info("User with id = {} has been updated with picture.", id);
        }
    }

    @Override
    public void deleteUserById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            log.info("User with id = {} has been found.", id);
        } else {
            log.warn("User with id = {} hasn't been found.", id);
            throw new EntityNotFoundException("User not found!");
        }

        if (lessonService.numberOfUserLessons(id) == 0) {
            userRepository.markAsDeletedById(id);
            log.info("User with id = {} has been deleted.", id);
        } else {
            log.warn("User with id = {} hasn't been deleted.", id);
            throw new UserCanNotBeDeletedException("User has lessons!");
        }
    }

    @Override
    public List<UserViewDto> listUsers() {
        final List<UserEntity> userEntities = userRepository.findAll();
        if (userEntities.size() != 0) {
            log.info("Users have been found.");
        } else {
            log.warn("There haven't been users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    public List<UserViewDto> listTrainers() {
        final List<UserEntity> userEntities = userRepository.findAllByRole(Role.TRAINER);
        if (userEntities.size() != 0) {
            log.info("Users have been found.");
        } else {
            log.warn("There haven't been users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

}
