package com.dataart.dancestudio.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserEntityService<T> {

    List<T> listUserEntities(int userId, Pageable pageable);

    int numberOfUserEntities(int userId);

}
