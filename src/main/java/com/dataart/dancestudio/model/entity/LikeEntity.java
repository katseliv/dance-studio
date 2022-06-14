package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@Entity(name = "likes")
@Table(name = "likes")
public class LikeEntity {

    @EmbeddedId
    private LikeId id;

    @Column(name = "is_deleted")
    private boolean deleted;

}
