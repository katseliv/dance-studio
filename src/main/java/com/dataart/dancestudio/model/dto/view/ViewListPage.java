package com.dataart.dancestudio.model.dto.view;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewListPage<T> {

    private final int pageSize;
    private final int totalPages;
    private final int additive;
    private final int startPageNumber;
    private final int currentPageNumber;
    private final int endPageNumber;
    private final List<T> viewDtoList;

}
