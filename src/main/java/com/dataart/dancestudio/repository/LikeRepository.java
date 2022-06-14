package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.LikeEntity;
import com.dataart.dancestudio.model.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId>, JpaSpecificationExecutor<LikeEntity> {
}
