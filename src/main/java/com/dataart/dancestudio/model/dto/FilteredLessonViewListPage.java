package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FilteredLessonViewListPage {

    private final String trainerName;
    private final String danceStyleName;
    private final String date;
    private final int pageSize;
    private final int totalPages;
    private final int additive;
    private final int startPageNumber;
    private final int endPageNumber;
    private final List<LessonViewDto> lessonViewDtoList;

    public static FilteredLessonViewListPageBuilder builder() {
        return new FilteredLessonViewListPageBuilder();
    }

    public static class FilteredLessonViewListPageBuilder {

        private String trainerName;
        private String danceStyleName;
        private String date;
        private int pageSize;
        private int totalPages;
        private int additive;
        private int startPageNumber;
        private int endPageNumber;
        private List<LessonViewDto> lessonViewDtoList;

        private boolean isUsed = false;

        public FilteredLessonViewListPageBuilder() {
        }

        public FilteredLessonViewListPageBuilder trainerName(final String trainerName) {
            this.trainerName = trainerName;
            return this;
        }

        public FilteredLessonViewListPageBuilder danceStyleName(final String danceStyleName) {
            this.danceStyleName = danceStyleName;
            return this;
        }

        public FilteredLessonViewListPageBuilder date(final String date) {
            this.date = date;
            return this;
        }

        public FilteredLessonViewListPageBuilder pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public FilteredLessonViewListPageBuilder totalPages(final int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public FilteredLessonViewListPageBuilder additive(final int additive) {
            this.additive = additive;
            return this;
        }

        public FilteredLessonViewListPageBuilder startPageNumber(final int startPageNumber) {
            this.startPageNumber = startPageNumber;
            return this;
        }

        public FilteredLessonViewListPageBuilder endPageNumber(final int endPageNumber) {
            this.endPageNumber = endPageNumber;
            return this;
        }

        public FilteredLessonViewListPageBuilder lessonViewDtoList(final List<LessonViewDto> lessonViewDtoList) {
            this.lessonViewDtoList = lessonViewDtoList;
            return this;
        }

        public FilteredLessonViewListPage build() {
            if (!isUsed) {
                isUsed = true;
                return new FilteredLessonViewListPage(trainerName, danceStyleName, date, pageSize, totalPages, additive, startPageNumber, endPageNumber, lessonViewDtoList);
            }
            throw new RuntimeException("Builder already built");
        }

    }

}
