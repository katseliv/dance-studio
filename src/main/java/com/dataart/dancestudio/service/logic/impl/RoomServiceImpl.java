package com.dataart.dancestudio.service.logic.impl;

import com.dataart.dancestudio.db.repository.impl.RoomRepository;
import com.dataart.dancestudio.service.logic.RoomService;
import com.dataart.dancestudio.service.mapper.RoomMapper;
import com.dataart.dancestudio.service.model.view.RoomViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Autowired
    public RoomServiceImpl(RoomRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<RoomViewDto> getAllRooms() {
        return mapper.fromEntities(repository.findAll());
    }

}
