package com.dataart.dancestudio.db.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

    private Integer id;

    private String username;

    private String firstName;

    private String lastName;

    private byte[] image;

    private String email;

    private String phoneNumber;

    private String password;

    private RoleEntity role;

    private String timeZone;

    private Boolean isDeleted;

}
