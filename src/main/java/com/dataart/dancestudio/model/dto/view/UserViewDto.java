package com.dataart.dancestudio.model.dto.view;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserViewDto {

    private final Integer id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String image;
    private final String email;
    private final String phoneNumber;

}
