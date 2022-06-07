package com.dataart.dancestudio.model.dto.view;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonViewDto {

    private final Integer id;
    private final String trainerFirstName;
    private final String trainerLastName;
    private final String danceStyleName;
    private final LocalDateTime startDatetime;

}
