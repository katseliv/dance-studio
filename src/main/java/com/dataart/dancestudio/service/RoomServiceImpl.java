package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.RoomMapper;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        final List<RoomEntity> roomEntities = roomRepository.findAll();
        if (roomEntities.size() != 0) {
            log.info("Rooms were found.");
        } else {
            log.info("There weren't rooms.");
        }
        return roomMapper.roomEntitiesToRoomViewDtoList(roomEntities);
    }

}
