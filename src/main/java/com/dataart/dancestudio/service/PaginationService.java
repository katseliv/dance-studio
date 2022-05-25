package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.view.ViewListPage;

public interface PaginationService<T> {

    ViewListPage<T> getViewListPage(String page, String size);

    ViewListPage<T> getUserViewListPage(Integer id, String page, String size);

    FilteredLessonViewListPage getFilteredLessonViewListPage(String page, String size, String trainerName,
                                                             String danceStyleName, String date);

}
