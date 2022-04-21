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
@Entity(name = "lessons")
@Where(clause = "is_deleted = false")
@Table(name = "lessons", schema = "dancestudio")
public class LessonEntity {

    private LessonEntity(final Integer id, final NewUserEntity userTrainer, final DanceStyleEntity danceStyle,
                         final LocalDateTime startDatetime, final Integer duration, final RoomEntity room,
                         final Boolean isDeleted) {
        this.id = id;
        this.userTrainer = userTrainer;
        this.danceStyle = danceStyle;
        this.startDatetime = startDatetime;
        this.duration = duration;
        this.room = room;
        this.isDeleted = isDeleted;
    }

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

