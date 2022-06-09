package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityAlreadyExistsException;
import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.exception.UserCanNotBeDeletedException;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDetailsDto;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.UserRegistrationDto;
import com.dataart.dancestudio.model.dto.view.UserForListDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.model.Provider;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import com.dataart.dancestudio.utils.ParseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, PaginationService<UserForListDto> {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;

    private final LessonService lessonService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public int createUser(final UserRegistrationDto userRegistrationDto, final Provider provider) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            log.warn("User with email = {} hasn't been created. Such user already exists!", userRegistrationDto.getEmail());
            throw new EntityAlreadyExistsException("User already exists in the database!");
        }
        final String password = passwordEncoder.encode(userRegistrationDto.getPassword());
        final UserEntity userEntity = Optional.of(userRegistrationDto)
                .map(user -> userMapper.userRegistrationDtoToUserEntityWithPassword(user, password))
                .map(user -> {
                    user.setImage(new byte[0]);
                    user.setRole(Role.USER);
                    user.setProvider(provider);
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
    public UserDetailsDto getUserDetailsById(final int id) {
        final Optional<UserEntity> userEntity = userRepository.findById(id);
        userEntity.ifPresentOrElse(
                (user) -> log.info("User with id = {} has been found.", user.getId()),
                () -> log.warn("User with id = {} hasn't been found.", id));
        return userEntity.map(userMapper::userEntityToUserDetailsDto)
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
    public boolean existsByUserEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            log.info("User with email = {} exists.", email);
            return true;
        } else {
            log.warn("User with email = {} doesn't exist.", email);
            return false;
        }
    }

    @Override
    @Transactional
    public void updateUserById(final UserDto userDto, final int id) {
        final UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        final String username = userDto.getUsername();
        if (!Objects.equals(username, userEntity.getUsername()) && userRepository.existsByUsername(username)) {
            log.warn("User with username = {} hasn't been updated. Such username already exists in the database!", username);
            throw new EntityAlreadyExistsException("Such username already exists!");
        }

        final String email = userDto.getEmail();
        if (!Objects.equals(email, userEntity.getEmail()) && userRepository.existsByEmail(email)) {
            log.warn("User with email = {} hasn't been updated. Such email already exists in the database!", email);
            throw new EntityAlreadyExistsException("Such email already exists!");
        }

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
        if (lessonService.numberOfUserLessons(id) > 0) {
            log.warn("User with id = {} hasn't been deleted. User has lessons!", id);
            throw new UserCanNotBeDeletedException("User has lessons!");
        }
        userRepository.markAsDeletedById(id);
        log.info("User with id = {} has been deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<UserForListDto> getViewListPage(final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<UserForListDto> listBookings = listUsers(pageable);
        final int totalAmount = numberOfUsers();

        return getViewListPage(totalAmount, pageSize, pageNumber, listBookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserForListDto> listTrainers(final Pageable pageable) {
        final List<UserEntity> userEntities = userRepository.findAllByRole(Role.TRAINER, pageable);
        log.info("There have been found {} trainers.", userEntities.size());
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    public List<UserForListDto> listUsers(final Pageable pageable) {
        final List<UserEntity> userEntities = userRepository.findAll(pageable).getContent();
        log.info("There have been found {} users.", userEntities.size());
        return userMapper.userEntitiesToUserViewDtoList(userEntities);
    }

    @Override
    public int numberOfUsers() {
        final long numberOfUsers = userRepository.count();
        log.info("There have been found {} users.", numberOfUsers);
        return (int) numberOfUsers;
    }

}
