package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.RoomMapper;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
