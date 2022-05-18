package com.dataart.dancestudio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ApiErrorDto {

    private final String status;
    private final String error;
    private final List<String> messages;

}
