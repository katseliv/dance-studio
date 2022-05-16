package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.DanceStyleMapper;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.repository.DanceStyleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class DanceStyleServiceImpl implements DanceStyleService {

    private final DanceStyleRepository danceStyleRepository;
    private final DanceStyleMapper danceStyleMapper;

    @Autowired
    public DanceStyleServiceImpl(final DanceStyleRepository danceStyleRepository,
                                 final DanceStyleMapper danceStyleMapper) {
        this.danceStyleRepository = danceStyleRepository;
        this.danceStyleMapper = danceStyleMapper;
    }

    @Override
    public List<DanceStyleViewDto> listDanceStyleViews() {
        final List<DanceStyleEntity> danceStyleEntities = danceStyleRepository.findAll();
        if (danceStyleEntities.size() != 0) {
            log.info("Dance Styles were found.");
        } else {
            log.info("There weren't dance styles.");
        }
        return danceStyleMapper.danceStyleEntitiesToDanceStyleViewDtoList(danceStyleEntities);
    }

}
