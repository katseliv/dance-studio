package com.dataart.dancestudio.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@Entity(name = "bookings")
@Where(clause = "is_deleted = false")
@Table(name = "bookings", schema = "dancestudio")
public class BookingEntity {

    public BookingEntity() {
    }

    private BookingEntity(final Integer id, final NewUserEntity user, final NewLessonEntity lesson, final Boolean isDeleted) {
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
    private NewLessonEntity lesson;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
