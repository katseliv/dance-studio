package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@Entity(name = "users")
@Where(clause = "is_deleted = false")
@Table(name = "users", schema = "dancestudio")
public class NewUserEntity {

    private NewUserEntity(final Integer id, final String username, final String firstName, final String lastName,
                          final byte[] image, final String email, final String phoneNumber, final String password,
                          final Role role, final String timeZone, final Boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.timeZone = timeZone;
        this.isDeleted = isDeleted;
    }

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
