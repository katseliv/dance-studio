package com.dataart.dancestudio.service;

import org.springframework.data.domain.PageRequest;

public interface PaginationService {

    PageRequest initPageRequest(Integer page, Integer size);

}
