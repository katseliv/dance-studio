package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsTest {

    private String email;
    private String password;

    public UserDetailsTest() {
    }

}
