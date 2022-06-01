package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DanceStyleService {

    List<DanceStyleViewDto> listDanceStyles(Pageable pageable);

    int numberOfDanceStyles();

}
