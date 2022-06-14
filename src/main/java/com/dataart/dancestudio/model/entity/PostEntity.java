package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Where(clause = "is_deleted = false")
@Entity(name = "posts")
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_admin_id", referencedColumnName = "id")
    private UserEntity userAdmin;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "text")
    private String text;

    @Column(name = "creation_datetime")
    private LocalDateTime creationDatetime;

    @Column(name = "is_deleted")
    private boolean deleted;

}
