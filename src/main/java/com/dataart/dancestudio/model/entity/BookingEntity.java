package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@Entity(name = "bookings")
@Where(clause = "is_deleted = false")
@Table(name = "bookings", schema = "dancestudio")
public class BookingEntity {

    private BookingEntity(final Integer id, final NewUserEntity user, final LessonEntity lesson, final Boolean isDeleted) {
        this.id = id;
        this.user = user;
        this.lesson = lesson;
        this.isDeleted = isDeleted;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NewUserEntity user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    private LessonEntity lesson;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
