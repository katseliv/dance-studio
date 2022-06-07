package com.dataart.dancestudio.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ApiErrorDto {

    private final String status;
    private final String error;
    private final List<String> messages;

}
