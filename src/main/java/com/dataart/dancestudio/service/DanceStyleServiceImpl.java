package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.DanceStyleMapper;
import com.dataart.dancestudio.model.dto.view.DanceStyleViewDto;
import com.dataart.dancestudio.model.entity.DanceStyleEntity;
import com.dataart.dancestudio.repository.DanceStyleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DanceStyleServiceImpl implements EntityService<DanceStyleViewDto> {

    private final DanceStyleRepository danceStyleRepository;
    private final DanceStyleMapper danceStyleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DanceStyleViewDto> listEntities(final Pageable pageable) {
        final List<DanceStyleEntity> danceStyleEntities = danceStyleRepository.findAll(pageable).getContent();
        if (danceStyleEntities.size() != 0) {
            log.info("Dance Styles have been found.");
        } else {
            log.info("There haven't been dance styles.");
        }
        return danceStyleMapper.danceStyleEntitiesToDanceStyleViewDtoList(danceStyleEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfEntities() {
        final long numberOfDanceStyles = danceStyleRepository.count();
        if (numberOfDanceStyles != 0) {
            log.info("There have been dance styles.");
        } else {
            log.warn("There haven't been dance styles.");
        }
        return (int) numberOfDanceStyles;
    }

}
