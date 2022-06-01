package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.ViewListPage;

import java.util.List;

public interface PaginationService<T> {

    int getButtonLimit();

    default ViewListPage<T> getViewListPage(final double totalAmount, final int pageSize, final int pageNumber,
                                            final List<T> userViewDtoList) {
        final int totalPages = (int) Math.ceil(totalAmount / pageSize);
        final int additive = (pageNumber - 1) * pageSize + 1;
        final int startPageNumber = getStartPageNumber(totalPages, pageNumber);
        final int endPageNumber = Math.max(Math.min(pageNumber + getButtonLimit() / 2, totalPages), getButtonLimit());

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

    default int getStartPageNumber(final int totalPages, final int pageNumber) {
        if (totalPages <= getButtonLimit()) {
            return 1;
        } else if (pageNumber > totalPages - getButtonLimit() / 2) {
            return totalPages - getButtonLimit() + 1;
        } else {
            return Math.max(pageNumber - getButtonLimit() / 2, 1);
        }
    }

}
