package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import com.dataart.dancestudio.repository.impl.UserRepository;
import com.dataart.dancestudio.service.UserService;
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
    public int createUser(final UserDto userDto) {
        try {
            final String password = passwordEncoder.encode(userDto.getPassword());
            return userRepository.save(userMapper.userDtoToUserEntityWithPassword(userDto, password));
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
        return -1;
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
    public void updateUserById(final UserDto userDto, final int id) {
        try {
            if (!userDto.getMultipartFile().isEmpty()) {
                userRepository.updateWithPicture(userMapper.userDtoToUserEntity(userDto), id);
            } else {
                final UserDto userDtoFromDB = getUserById(id);
                if (!hasToBeUpdated(userDto, userDtoFromDB)) {
                    userRepository.update(userMapper.userDtoToUserEntity(userDto), id);
                }
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }

    //TODO: think about naming
    private boolean hasToBeUpdated(final UserDto userDto, final UserDto userDtoFromDB) {
        return Objects.equals(userDto.getUsername(), userDtoFromDB.getUsername()) &&
                Objects.equals(userDto.getFirstName(), userDtoFromDB.getFirstName()) &&
                Objects.equals(userDto.getLastName(), userDtoFromDB.getLastName()) &&
                Objects.equals(userDto.getEmail(), userDtoFromDB.getEmail()) &&
                Objects.equals(userDto.getPhoneNumber(), userDtoFromDB.getPhoneNumber());
    }

    @Override
    public void deleteUserById(final int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserViewDto> listUsers() {
        return userMapper.userEntitiesToUserViewDtoList(userRepository.findAll());
    }

}
