package com.dataart.dancestudio.model.dto.view;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingViewDto {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String danceStyle;
    private final LocalDateTime startDatetime;

}
