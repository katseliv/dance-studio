package com.dataart.dancestudio.service;

import com.dataart.dancestudio.mapper.RoomMapperImpl;
import com.dataart.dancestudio.model.dto.view.RoomViewDto;
import com.dataart.dancestudio.model.entity.RoomEntity;
import com.dataart.dancestudio.model.entity.StudioEntity;
import com.dataart.dancestudio.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Spy
    private RoomMapperImpl roomMapperImpl;

    @Mock
    private RoomRepository roomRepositoryMock;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    private final int id = 1;
    private final int studioId = 1;
    private final String name = "Big";
    private final String description = "Description";

    private final StudioEntity studioEntity = StudioEntity.builder()
            .name(name)
            .description(description)
            .build();
    private final RoomEntity roomEntity = RoomEntity.builder()
            .id(id)
            .name(name)
            .studio(studioEntity)
            .description(description)
            .build();
    private final RoomViewDto roomViewDto = RoomViewDto.builder()
            .id(id)
            .name(name)
            .studioId(studioId)
            .description(description)
            .build();

    @Test
    public void listRooms() {
        // given
        final List<RoomViewDto> roomViewDtoListExpected = List.of(roomViewDto);
        final List<RoomEntity> roomEntities = List.of(roomEntity);

        when(roomMapperImpl.roomEntitiesToRoomViewDtoList(roomEntities)).thenReturn(roomViewDtoListExpected);
        when(roomRepositoryMock.findAll()).thenReturn(roomEntities);

        // when
        final List<RoomViewDto> bookingViewDtoListActual = roomServiceImpl.listRooms();

        // then
        verify(roomRepositoryMock, times(1)).findAll();
        assertEquals(roomViewDtoListExpected, bookingViewDtoListActual);
    }

}
