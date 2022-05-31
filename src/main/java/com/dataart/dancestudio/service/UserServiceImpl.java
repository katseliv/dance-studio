package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, EntityService<UserForListDto> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEntityService<LessonViewDto> userLessonService;

    @Override
    @Transactional
    public int createUser(final UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            log.warn("User hasn't been created.");
            throw new EntityAlreadyExistsException("User already exists in the database!");
        }
        final UserEntity userEntity = Optional.of(userRegistrationDto)
                .map(user -> {
                    final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
                    return userMapper.userRegistrationDtoToUserEntityWithPassword(user, password);
                })
                .map(user -> {
                    user.setImage(new byte[0]);
                    user.setRole(Role.USER);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityCreationException("User not created!"));

        log.info("User with id = {} has been created.", userEntity.getId());
        return userEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", user.getId()),
                () -> log.warn("User with id = {} hasn't been found.", id));
        return userEntity.map(userMapper::userEntityToUserDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserViewDto getUserViewById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", user.getId()),
                () -> log.warn("User with id = {} hasn't been found.", id));
        return userEntity.map(userMapper::userEntityToUserViewDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserIdByEmail(final String email) {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with email = {} has been found.", email),
                () -> log.warn("User with email = {} hasn't been found.", email));
        return userEntity.map(userMapper::userEntityToUserDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found!")).getId();
    }

    @Override
    public UserDetailsDto getUserByEmail(final String email) {
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User for email = {} with id = {} has been found.", email, user.getId()),
                () -> log.warn("User for email = {} hasn't been found.", email));
        return userMapper.userEntityToUserDetailsDto(userEntity.orElseThrow(
                () -> new UsernameNotFoundException("No such user in the database!")));
    }

    @Override
    @Transactional
    public void updateUserById(final UserDto userDto, final int id) {
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        if (userDto.getBase64StringImage().isEmpty()) {
            userMapper.mergeUserEntityAndUserDtoWithoutPicture(userEntity, userDto);
            userRepository.save(userEntity);
            log.info("User with id = {} has been updated without picture.", id);
        } else {
            userMapper.mergeUserEntityAndUserDto(userEntity, userDto);
            userRepository.save(userEntity);
            log.info("User with id = {} has been updated with picture.", id);
        }
    }

    @Override
    @Transactional
    public void deleteUserById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", id),
                () -> {
                    log.warn("User with id = {} hasn't been found.", id);
                    throw new EntityNotFoundException("User not found!");
                });
        if (userLessonService.numberOfUserEntities(id) > 0) {
            log.warn("User with id = {} hasn't been deleted.", id);
            throw new UserCanNotBeDeletedException("User has lessons!");
        }
        userRepository.markAsDeletedById(id);
        log.info("User with id = {} has been deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserForListDto> listTrainers(final Pageable pageable) {
        final List<UserEntity> userEntities = userRepository.findAllByRole(Role.TRAINER, pageable);
        if (userEntities.size() != 0) {
            log.info("Users have been found.");
        } else {
            log.warn("There haven't been users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserForListDto> listEntities(final Pageable pageable) {
        final List<UserEntity> userEntities = userRepository.findAll(pageable).getContent();
        if (userEntities.size() != 0) {
            log.info("Users have been found.");
        } else {
            log.warn("There haven't been users.");
        }
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfEntities() {
        final long numberOfUsers = userRepository.count();
        if (numberOfUsers != 0) {
            log.info("There have been users.");
        } else {
            log.warn("There haven't been users.");
        }
        return (int) numberOfUsers;
    }

}
