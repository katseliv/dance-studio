package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "rooms")
@Where(clause = "is_deleted = false")
@Table(name = "rooms", schema = "dancestudio")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "studio_id", referencedColumnName = "id")
    private StudioEntity studio;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
