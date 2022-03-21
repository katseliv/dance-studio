package com.dataart.dancestudio.db.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    void save(T t);

    Optional<T> findById(int id);

    void update(T t, int id);

    void deleteById(int id);

    List<T> findAll();

}
