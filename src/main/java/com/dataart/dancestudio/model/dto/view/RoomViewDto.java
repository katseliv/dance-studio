package com.dataart.dancestudio.model.dto.view;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomViewDto {

    private final Integer id;
    private final String name;
    private final String description;
    private final Integer studioId;
    private final Boolean isDeleted;

}
