package com.dataart.dancestudio.model.dto;

import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserLessonViewListPage {

    private final int pageSize;
    private final int totalPages;
    private final int additive;
    private final int startPageNumber;
    private final int endPageNumber;
    private final List<LessonViewDto> userLessonViewDtoList;

    public static UserLessonViewListPageBuilder builder() {
        return new UserLessonViewListPageBuilder();
    }

    public static class UserLessonViewListPageBuilder {

        private int pageSize;
        private int totalPages;
        private int additive;
        private int startPageNumber;
        private int endPageNumber;
        private List<LessonViewDto> userLessonViewDtoList;

        private boolean isUsed = false;

        public UserLessonViewListPageBuilder() {
        }

        public UserLessonViewListPageBuilder pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public UserLessonViewListPageBuilder totalPages(final int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public UserLessonViewListPageBuilder additive(final int additive) {
            this.additive = additive;
            return this;
        }

        public UserLessonViewListPageBuilder startPageNumber(final int startPageNumber) {
            this.startPageNumber = startPageNumber;
            return this;
        }

        public UserLessonViewListPageBuilder endPageNumber(final int endPageNumber) {
            this.endPageNumber = endPageNumber;
            return this;
        }

        public UserLessonViewListPageBuilder userLessonViewDtoList(final List<LessonViewDto> userLessonViewDtoList) {
            this.userLessonViewDtoList = userLessonViewDtoList;
            return this;
        }

        public UserLessonViewListPage build() {
            if (!isUsed) {
                isUsed = true;
                return new UserLessonViewListPage(pageSize, totalPages, additive, startPageNumber, endPageNumber, userLessonViewDtoList);
            }
            throw new RuntimeException("Builder already built");
        }

    }
}
