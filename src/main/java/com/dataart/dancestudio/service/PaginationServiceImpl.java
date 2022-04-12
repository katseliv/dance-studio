package com.dataart.dancestudio.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaginationServiceImpl implements PaginationService {

    @Override
    public PageRequest initPageRequest(final Integer page, final Integer size) {
        final int defaultPageNumber = 1;
        final int defaultPageSize = 5;

        Integer pageNumber = Optional.ofNullable(page).orElse(defaultPageNumber);
        final Integer pageSize = Optional.ofNullable(size).orElse(defaultPageSize);

        if (pageNumber == 0) {
            pageNumber = 1;
        }
        return PageRequest.of(pageNumber - 1, pageSize);
    }

}
