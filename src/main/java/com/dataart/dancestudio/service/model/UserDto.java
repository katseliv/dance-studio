package com.dataart.dancestudio.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;

@Getter
@Setter
public class UserDto {

    private String username;

    private String firstName;

    private String lastName;

    private MultipartFile multipartFile;

    private String email;

    private String phoneNumber;

    private String password;

    private Integer roleId;

    private ZoneId timeZone;

    private Boolean isDeleted;

}
