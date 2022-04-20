package com.dataart.dancestudio.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@Builder
@Entity(name = "studios")
@Where(clause = "is_deleted = false")
@Table(name = "studios", schema = "dancestudio")
public class StudioEntity {

    public StudioEntity() {
    }

    private StudioEntity(final Integer id, final String name, final String description, final LocalTime startTime,
                         final LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

}
