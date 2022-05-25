package com.dataart.dancestudio.model.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class UserForListDto {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;

}
