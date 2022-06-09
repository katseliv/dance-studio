package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.ViewListPage;

import java.util.List;

public interface PaginationService<T> {

    default ViewListPage<T> getViewListPage(final double totalAmount, final int pageSize, final int pageNumber,
                                            final List<T> userViewDtoList) {
        final int totalPages = (int) Math.ceil(totalAmount / pageSize);
        return ViewListPage.<T>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .viewDtoList(userViewDtoList)
                .build();
    }

}
