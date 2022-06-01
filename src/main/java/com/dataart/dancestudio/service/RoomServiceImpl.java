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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public List<RoomViewDto> listRooms(final Pageable pageable) {
        final List<RoomEntity> roomEntities = roomRepository.findAll(pageable).getContent();
        log.info("There have been found {} rooms.", roomEntities.size());
        return roomMapper.roomEntitiesToRoomViewDtoList(roomEntities);
    }

    @Transactional(readOnly = true)
    public int numberOfRooms() {
        final long numberOfRooms = roomRepository.count();
        log.info("There have been found {} rooms.", numberOfRooms);
        return (int) numberOfRooms;
    }

}
