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
public class DanceStyleServiceImpl implements DanceStyleService {

    private final DanceStyleRepository danceStyleRepository;
    private final DanceStyleMapper danceStyleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DanceStyleViewDto> listDanceStyles(final Pageable pageable) {
        final List<DanceStyleEntity> danceStyleEntities = danceStyleRepository.findAll(pageable).getContent();
        log.info("There have been found {} dance styles.", danceStyleEntities.size());
        return danceStyleMapper.danceStyleEntitiesToDanceStyleViewDtoList(danceStyleEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfDanceStyles() {
        final long numberOfDanceStyles = danceStyleRepository.count();
        log.info("There have been found {} dance styles.", numberOfDanceStyles);
        return (int) numberOfDanceStyles;
    }

}
