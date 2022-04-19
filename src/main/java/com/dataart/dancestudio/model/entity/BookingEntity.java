package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "bookings")
@Where(clause = "is_deleted = false")
@Table(name = "bookings", schema = "dancestudio")
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
