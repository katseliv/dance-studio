package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanceStyleRepository extends JpaRepository<DanceStyleEntity, Integer> {
}
