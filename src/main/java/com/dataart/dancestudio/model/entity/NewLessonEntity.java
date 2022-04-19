package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "lessons", schema = "dancestudio")
@Entity(name = "lessons")
public class NewLessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_trainer_id", referencedColumnName = "id")
    private NewUserEntity userTrainer;

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
    private Boolean isDeleted;

}
