package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.UserLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class LessonPaginationServiceImpl implements LessonPaginationService {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;
    @Value("${pagination.buttonLimit}")
    private int buttonLimit;

    private final LessonService lessonService;

    @Autowired
    public LessonPaginationServiceImpl(final LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Override
    public FilteredLessonViewListPage getFilteredLessonViewListPage(final String page, final String size,
                                                                    final String trainerName, final String danceStyleName,
                                                                    final String date) {
        final int pageNumber = Optional.ofNullable(page).map(Integer::parseInt).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(Integer::parseInt).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final List<LessonViewDto> lessonViewDtoList = lessonService.listLessons(trainerName, danceStyleName, date, pageable);
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
    public UserLessonViewListPage getUserLessonViewListPage(final Integer id, final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(Integer::parseInt).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(Integer::parseInt).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        final List<LessonViewDto> lessonViewDtoList = lessonService.listUserLessons(id, pageable);
        final int totalAmount = lessonService.numberOfUserLessons(id);

        final int totalPages = (int) Math.ceil((double) totalAmount / pageSize);
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int additive = (pageNumber - 1) * pageSize + 1;
        final int endPageNumber = Math.max(Math.min(pageNumber + buttonLimit / 2, totalPages), buttonLimit);

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
