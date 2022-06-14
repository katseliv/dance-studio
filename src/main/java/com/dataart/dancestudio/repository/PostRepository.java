package com.dataart.dancestudio.repository;

import com.dataart.dancestudio.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>, JpaSpecificationExecutor<PostEntity> {

    @Modifying
    @Query("UPDATE posts SET deleted = TRUE WHERE id = ?1")
    void markAsDeletedById(int id);

}
