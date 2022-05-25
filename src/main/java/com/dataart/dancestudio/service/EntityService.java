package com.dataart.dancestudio.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntityService<T> {

    List<T> listEntities(Pageable pageable);

    int numberOfEntities();

}
