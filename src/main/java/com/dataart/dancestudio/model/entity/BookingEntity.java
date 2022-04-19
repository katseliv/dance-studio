package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "bookings", schema = "dancestudio")
@Entity(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NewUserEntity user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    private NewLessonEntity lesson;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
