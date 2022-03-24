package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.repository.impl.UserRepository;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.model.dto.UserDto;
import com.dataart.dancestudio.model.dto.view.UserViewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public int createUser(final UserDto userDto) {
        try {
            return userRepository.save(userMapper.toEntity(userDto));
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public UserDto getUserById(final int id) {
        return userMapper.fromEntity(userRepository.findById(id).orElseThrow());
    }

    @Override
    public void updateUserById(final UserDto userDto, final int id) {
        try {
            userRepository.update(userMapper.toEntity(userDto), id);
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void deleteUserById(final int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserViewDto> getAllUsers() {
        return userMapper.fromEntities(userRepository.findAll());
    }

}
