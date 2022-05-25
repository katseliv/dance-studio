package com.dataart.dancestudio.model.dto.view;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DanceStyleViewDto {

    private final Integer id;
    private final String name;
    private final String description;

}
