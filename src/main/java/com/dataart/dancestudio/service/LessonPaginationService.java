package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.FilteredLessonViewListPage;
import com.dataart.dancestudio.model.dto.UserLessonViewListPage;

public interface LessonPaginationService {

    FilteredLessonViewListPage getFilteredLessonViewListPage(String page, String size, String trainerName,
                                                             String danceStyleName, String date);

    UserLessonViewListPage getUserLessonViewListPage(Integer id, String page, String size);

}
