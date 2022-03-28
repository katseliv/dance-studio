package com.dataart.dancestudio.service.impl;

import com.dataart.dancestudio.repository.impl.RoomRepository;
import com.dataart.dancestudio.service.RoomService;
import com.dataart.dancestudio.mapper.RoomMapper;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomServiceImpl(final RoomRepository roomRepository, final RoomMapper mapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = mapper;
    }

    @Override
    public List<RoomViewDto> listRooms() {
        return roomMapper.roomEntitiesToRoomViewDtoList(roomRepository.findAll());
    }

}
