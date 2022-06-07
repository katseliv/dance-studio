package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {

    List<RoomViewDto> listRooms(Pageable pageable);

    int numberOfRooms();

}
