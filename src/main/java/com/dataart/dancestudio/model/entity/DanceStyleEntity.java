package com.dataart.dancestudio.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@Table(name = "dance_styles", schema = "dancestudio")
@Entity(name = "dance_styles")
public class DanceStyleEntity {

    public DanceStyleEntity() {
    }

    private DanceStyleEntity(final Integer id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
