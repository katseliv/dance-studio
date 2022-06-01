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
@Entity(name = "lessons")
@Table(name = "lessons")
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_trainer_id", referencedColumnName = "id")
    private UserEntity userTrainer;

    @ManyToOne
    @JoinColumn(name = "dance_style_id", referencedColumnName = "id")
    private DanceStyleEntity danceStyle;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "duration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private RoomEntity room;

    @Column(name = "is_deleted")
    private boolean deleted;

}

