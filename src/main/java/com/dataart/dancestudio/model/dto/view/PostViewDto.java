package com.dataart.dancestudio.model.dto.view;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostViewDto {

    private final Integer id;
    private final String authorFirstName;
    private final String authorLastName;
    private final String base64StringImage;
    private final String text;
    private final String creationDatetime;

}
