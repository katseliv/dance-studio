package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaginationServiceImpl<T> implements PaginationService<T> {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;
    @Value("${pagination.buttonLimit}")
    private int buttonLimit;

    private final EntityService<T> entityService;
    private final UserEntityService<T> userEntityService;
    private final LessonService lessonService;

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<T> getViewListPage(final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(Integer::parseInt).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(Integer::parseInt).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<T> listEntities = entityService.listEntities(pageable);
        final int totalAmount = entityService.numberOfEntities();

        return getViewListPage(totalAmount, pageSize, pageNumber, listEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<T> getUserViewListPage(final Integer id, final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(Integer::parseInt).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(Integer::parseInt).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<T> listUserEntities = userEntityService.listUserEntities(id, pageable);
        final int totalAmount = userEntityService.numberOfUserEntities(id);

        return getViewListPage(totalAmount, pageSize, pageNumber, listUserEntities);
    }

    @Override
    @Transactional(readOnly = true)
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

    private ViewListPage<T> getViewListPage(final double totalAmount, final int pageSize, final int pageNumber,
                                            final List<T> userViewDtoList) {
        final int totalPages = (int) Math.ceil(totalAmount / pageSize);
        final int additive = (pageNumber - 1) * pageSize + 1;
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int endPageNumber = Math.max(Math.min(pageNumber + buttonLimit / 2, totalPages), buttonLimit);

        return ViewListPage.<T>builder()
                .pageSize(pageSize)
                .totalPages(totalPages)
                .additive(additive)
                .startPageNumber(startPageNumber)
                .currentPageNumber(pageNumber)
                .endPageNumber(endPageNumber)
                .viewDtoList(userViewDtoList)
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
