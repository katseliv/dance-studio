package com.dataart.dancestudio.model.dto.view;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilteredLessonViewListPage {

    private final String trainerName;
    private final String danceStyleName;
    private final String date;
    private final int pageSize;
    private final int totalPages;
    private final int additive;
    private final int startPageNumber;
    private final int currentPageNumber;
    private final int endPageNumber;
    private final List<LessonViewDto> lessonViewDtoList;

}
