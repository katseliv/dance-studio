package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.RoomMapper;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements EntityService<RoomViewDto> {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomViewDto> listEntities(final Pageable pageable) {
        final List<RoomEntity> roomEntities = roomRepository.findAll(pageable).getContent();
        if (roomEntities.size() != 0) {
            log.info("Rooms have been found.");
        } else {
            log.info("There haven't been rooms.");
        }
        return roomMapper.roomEntitiesToRoomViewDtoList(roomEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public int numberOfEntities() {
        final long numberOfRooms = roomRepository.count();
        if (numberOfRooms != 0) {
            log.info("There have been rooms.");
        } else {
            log.warn("There haven't been rooms.");
        }
        return (int) numberOfRooms;
    }

}
