package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.UserLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonPaginationServiceImpl implements LessonPaginationService {

    private static final int defaultPageNumber = 1;
    private static final int defaultPageSize = 5;
    private static final int buttonLimit = 5;

    private final LessonService lessonService;

    @Autowired
    public LessonPaginationServiceImpl(final LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Override
    public FilteredLessonViewListPage getFilteredLessonViewListPage(final Integer page, final Integer size, final String trainerName, final String danceStyleName, final String date) {
        final Integer pageNumber = Optional.ofNullable(page).orElse(defaultPageNumber);
        final Integer pageSize = Optional.ofNullable(size).orElse(defaultPageSize);

        final List<LessonViewDto> lessonViewDtoList = lessonService.listLessons(trainerName, danceStyleName, date, pageSize, (pageNumber - 1) * pageSize);
        final int totalAmount = lessonService.numberOfFilteredLessons(trainerName, danceStyleName, date);

        final int totalPages = (int) Math.ceil((double) totalAmount / pageSize);
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int endPageNumber = Math.max(Math.min(pageNumber + buttonLimit / 2, totalPages), buttonLimit);
        final int additive = (pageNumber - 1) * pageSize + 1;

        return FilteredLessonViewListPage.builder()
                .trainerName(trainerName)
                .danceStyleName(danceStyleName)
                .date(date)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .additive(additive)
                .startPageNumber(startPageNumber)
                .currentPageNumber(pageNumber)
                .endPageNumber(endPageNumber)
                .lessonViewDtoList(lessonViewDtoList)
                .build();
    }

    @Override
    public UserLessonViewListPage getUserLessonViewListPage(final Integer id, final Integer page, final Integer size) {
        final Integer pageNumber = Optional.ofNullable(page).orElse(defaultPageNumber);
        final Integer pageSize = Optional.ofNullable(size).orElse(defaultPageSize);

        final List<LessonViewDto> lessonViewDtoList = lessonService.listUserLessons(id, pageSize, (pageNumber - 1) * pageSize);
        final int totalAmount = lessonService.numberOfUserLessons(id);

        final int totalPages = (int) Math.ceil((double) totalAmount / pageSize);
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int endPageNumber = Math.max(Math.min(pageNumber + buttonLimit / 2, totalPages), buttonLimit);
        final int additive = (pageNumber - 1) * pageSize + 1;

        return UserLessonViewListPage.builder()
                .pageSize(pageSize)
                .totalPages(totalPages)
                .additive(additive)
                .startPageNumber(startPageNumber)
                .currentPageNumber(pageNumber)
                .endPageNumber(endPageNumber)
                .userLessonViewDtoList(lessonViewDtoList)
                .build();
    }

    private int getStartPageNumber(final int totalPages, final int pageNumber) {
        if (totalPages <= buttonLimit) {
            return 1;
        } else if (pageNumber > totalPages - buttonLimit / 2) {
            return totalPages - buttonLimit + 1;
        } else {
            return Math.max(pageNumber - buttonLimit / 2, 1);
        }
    }

}
