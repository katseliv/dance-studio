package com.dataart.dancestudio.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "users")
@Where(clause = "is_deleted = false")
@Table(name = "users", schema = "dancestudio")
public class UserEntity {

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username)
                && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName)
                && Arrays.equals(image, that.image) && Objects.equals(email, that.email)
                && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(password, that.password)
                && role == that.role && Objects.equals(timeZone, that.timeZone)
                && Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, firstName, lastName, email, phoneNumber, password, role, timeZone, isDeleted);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

}
