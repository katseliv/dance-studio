package com.dataart.dancestudio.service.logic.impl;

import com.dataart.dancestudio.db.repository.impl.DanceStyleRepository;
import com.dataart.dancestudio.service.logic.DanceStyleService;
import com.dataart.dancestudio.service.mapper.DanceStyleMapper;
import com.dataart.dancestudio.service.model.DanceStyleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanceStyleServiceImpl implements DanceStyleService {

    private final DanceStyleRepository repository;
    private final DanceStyleMapper mapper;

    @Autowired
    public DanceStyleServiceImpl(DanceStyleRepository repository, DanceStyleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DanceStyleDto> getAllDanceStyles() {
        return mapper.fromEntities(repository.findAll());
    }

}
