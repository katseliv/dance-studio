package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.repository.impl.DanceStyleRepository;
import com.dataart.dancestudio.service.DanceStyleService;
import com.dataart.dancestudio.mapper.DanceStyleMapper;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanceStyleServiceImpl implements DanceStyleService {

    private final DanceStyleRepository danceStyleRepository;
    private final DanceStyleMapper danceStyleMapper;

    @Autowired
    public DanceStyleServiceImpl(final DanceStyleRepository danceStyleRepository, final DanceStyleMapper danceStyleMapper) {
        this.danceStyleRepository = danceStyleRepository;
        this.danceStyleMapper = danceStyleMapper;
    }

    @Override
    public List<DanceStyleViewDto> listDanceStyleViews() {
        return danceStyleMapper.danceStyleEntitiesToDanceStyleViewDtoList(danceStyleRepository.list());
    }

}
