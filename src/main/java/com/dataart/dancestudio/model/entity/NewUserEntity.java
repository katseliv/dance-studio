package com.dataart.dancestudio.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "users")
@Where(clause = "is_deleted = false")
@Table(name = "users", schema = "dancestudio")
public class NewUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id")
    private Role role;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
