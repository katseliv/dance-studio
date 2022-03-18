package com.dataart.dancestudio.service.logic.impl;

import com.dataart.dancestudio.db.repository.impl.UserRepository;
import com.dataart.dancestudio.service.logic.UserService;
import com.dataart.dancestudio.service.mapper.UserMapper;
import com.dataart.dancestudio.service.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void createUser(UserDto userDto) {
        try {
            repository.save(mapper.toEntity(userDto));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto getUserById(int id) {
        return mapper.fromEntity(repository.findById(id).orElse(null));
    }

    @Override
    public void updateUserById(UserDto userDto, int id) {
        try {
            repository.update(mapper.toEntity(userDto), id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return mapper.fromEntities(repository.findAll());
    }

}
