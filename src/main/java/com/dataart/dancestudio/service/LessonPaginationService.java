package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.UserLessonViewListPage;

public interface LessonPaginationService {

    FilteredLessonViewListPage getFilteredLessonViewListPage(Integer page, Integer size, String trainerName,
                                                             String danceStyleName, String date);

    UserLessonViewListPage getUserLessonViewListPage(Integer id, Integer page, Integer size);

}
